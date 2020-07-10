package com.netty.handler;

import com.alibaba.fastjson.JSON;
import com.netty.pojo.SelfRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 10:27 上午
 **/
public class UserServerHandler extends ChannelInboundHandlerAdapter {

    private final Map<String,Object> cache =new HashMap<>();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SelfRequest selfRequest = JSON.parseObject(msg.toString(), SelfRequest.class);
        if (selfRequest!=null){
            Object target = findExcuteObject(selfRequest.getClassName());
            LinkedHashMap<String, Object> params = selfRequest.getParams();
            List<Class> paramsType=new ArrayList<>();
            List<Object> paramObject=new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Class<?> aClass = Class.forName(entry.getKey());
                paramsType.add(aClass);
                Object o = JSON.parseObject(entry.getValue().toString(),aClass);
                paramObject.add(o);
            }
            Method method = target.getClass().getMethod(selfRequest.getMethodName(), paramsType.toArray(new Class[0]));
            Object result = method.invoke(target, paramObject.toArray(new Object[0]));
            ctx.writeAndFlush(JSON.toJSONString(result));
        }
    }

    private Object findExcuteObject(String className) throws Exception {
        Object o = cache.get(className);
        if (o!=null){
            System.out.println("缓存获取目标类");
           return o;
        }
        Class<?> aClass = Class.forName(className);
        URL resource = aClass.getResource("/");
        System.out.println(resource);

        String fileName = resource.getFile();
        System.out.println(fileName);

        String pathName = fileName.replaceFirst("/", "");
        System.out.println(pathName);
        String scanPackagePath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        File rootFile = new File(scanPackagePath);
        List<Class> childrens=new ArrayList<>();
        setSubList(rootFile, rootFile.getPath() + "/", aClass,childrens);
        if (childrens.size()>1){
            throw new RuntimeException("no unique children");
        }
        Object target = childrens.get(0).newInstance();
        cache.put(className,target);
        return target;
    };

    public static <T> void setSubList(File rootFile, String parentDirectory,
                                      Class<T> parentClass,List<Class> classes) {
        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                setSubList(file, parentDirectory, parentClass,classes);
            }
        } else {
            String className = null;
            try {
                if (rootFile.getPath().indexOf(".class") != -1) {
                    className = rootFile.getPath().replace(parentDirectory, "")
                            .replace(".class", "").replace("/", ".");
                    Class<?> classObject = Class.forName(className);
                    classObject.asSubclass(parentClass);

                    // 要么是子类，要么是类本身
                    if (! className.equals(parentClass.getCanonicalName())){
                        System.out.println(className + " extends " + parentClass);
                        classes.add(Class.forName(className));
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println("can not find " + className);
            } catch (ClassCastException e) {
                System.err.println(className + " do not extends " + parentClass);
            }
        }
    }
}
