package com.qhh.edu.mvcframework.servlet;

import com.qhh.edu.mvcframework.annotations.*;
import com.qhh.edu.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author qinhaohao
 * @Date 2020/6/16 5:56 下午
 **/
public class QhhDispatcherServlet extends HttpServlet {

    private Properties properties=new Properties();

    // 缓存扫描到的类的全限定类名
    private List<String> classNames = new ArrayList<>();

    private Map<String,Object> ioc=new HashMap<>();

    private List<Handler> handlerMapping =new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        //1、加载配置文件springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        //2、扫描相关的类、扫描注解
        doScan(properties.getProperty("scanPackage"));
        //3、初始化bean对象
        doInstance();
        //4、实现依赖注入
        doAutowired();
        //5、构造一个handlerMapping映射器，将配置好的url和method建立映射关系
        initHandlerMapping();

        System.out.println("mvc初始化完毕，等待请求进入");
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(QhhController.class)) {
                continue;
            }
            String baseUrl = "";
            if (aClass.isAnnotationPresent(QhhRequestMapping.class)){
                QhhRequestMapping requestMapping = aClass.getAnnotation(QhhRequestMapping.class);
                baseUrl = requestMapping.value();
            }
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(QhhRequestMapping.class)) continue;
                QhhRequestMapping methodRequestMapping = method.getAnnotation(QhhRequestMapping.class);
                String methodUrl = methodRequestMapping.value();
                String url=baseUrl+methodUrl;
                Handler handler = new Handler(entry.getValue(), method, Pattern.compile(url));
                if (method.isAnnotationPresent(Security.class)){
                    Security annotation = method.getAnnotation(Security.class);
                    String securityValue = annotation.value();
                    String securityPattern = annotation.pattern();
                    handler.setSecurityValue(securityValue);
                    handler.setSecurityPattern(Pattern.compile(securityPattern));
                }
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    if (parameter.getType()==HttpServletRequest.class||parameter.getType()==HttpServletResponse.class){
                        // 如果是request和response对象，那么参数名称写HttpServletRequest和HttpServletResponse
                        handler.getParamIndexMapping().put(parameter.getType().getSimpleName(),i);
                    }else {
                        handler.getParamIndexMapping().put(parameter.getName(),i);
                    }
                }
                handlerMapping.add(handler);
            }
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (!field.isAnnotationPresent(QhhAutowird.class)){
                    continue;
                }
                QhhAutowird autowird = field.getAnnotation(QhhAutowird.class);
                String beanName = autowird.value();
                if ("".equals(beanName.trim())){
                    //如果没有配置字段id，就需要根据类型注入
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.size()==0) return;
        try {
            for (String className : classNames) {
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(QhhController.class)){
                    String simpleName = aClass.getSimpleName();
                    String lowerSimpleName = lowerFirst(simpleName);
                    Object o = aClass.newInstance();
                    ioc.put(lowerSimpleName,o);
                }else if (aClass.isAnnotationPresent(QhhService.class)){
                    QhhService service = aClass.getAnnotation(QhhService.class);
                    String beanName = service.value();
                    if (!"".equals(beanName.trim())){
                        ioc.put(beanName,aClass.newInstance());
                    }else {
                        beanName = lowerFirst(aClass.getSimpleName());
                        ioc.put(beanName,aClass.newInstance());
                    }
                    // service层往往是有接口的，面向接口开发，此时再以接口名为id，放入一份对象到ioc中，便于后期根据接口类型注入
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        String interfaceName = anInterface.getName();
                        ioc.put(interfaceName,aClass.newInstance());
                    }
                }else {
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    // 首字母小写方法
    public String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        if('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }
    private void doScan(String scanPackage) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(scanPackagePath);
        File[] files = pack.listFiles();
        for (File file : files) {
            if (file.isDirectory()){
                doScan(scanPackage+"."+file.getName());
            }else if (file.getName().endsWith(".class")){
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }

    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Handler handler=getHandler(req);

        if (handler==null){
            resp.getWriter().write("404 not found");
        }
        String security = req.getParameter("security");
        if (StringUtils.isEmpty(security)){
            resp.getWriter().write("No access");
            return;
        }
        if (!handler.getSecurityValue().contains(security)&&!handler.getSecurityPattern().matcher(security).matches()){
            resp.getWriter().write("No access");
            return;
        }
        Object controller = handler.getController();
        Method method = handler.getMethod();
        Map<String, Integer> paramIndexMapping = handler.getParamIndexMapping();
//        method.invoke(controller,)
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();

        Map<String, String[]> parameterMap = req.getParameterMap();
        Object[] paraValues = new Object[parameterTypes.length];

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            // name=1&name=2   name [1,2]
            String value= StringUtils.join(entry.getValue(),",");
            if (!paramIndexMapping.containsKey(entry.getKey())){continue;};
            Integer index = paramIndexMapping.get(entry.getKey());
            paraValues[index]=value;
        }
        Integer reqIndex = paramIndexMapping.get(HttpServletRequest.class.getSimpleName());
        if (reqIndex!=null){
            paraValues[reqIndex]=req;
        }
        Integer respIndex = paramIndexMapping.get(HttpServletResponse.class.getSimpleName());
        if (respIndex!=null){
            paraValues[respIndex]=resp;
        }
        try {
            Object result = method.invoke(controller, paraValues);
            resp.getWriter().write(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler getHandler(HttpServletRequest req) {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        String requestURI = req.getRequestURI();
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(requestURI);
            if (matcher.matches()){
                return handler;
            }
        }
        return null;
    }

}
