package com.study.pub.factory;

import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qinhaohao
 * @Date 2020-05-30 11:44
 **/
public class BeanFactory {

    private static Map<String,Object> beanMap=new HashMap<>();

    private static Map<Class,List<String>> beanClassMap=new HashMap<>();

    private static final int FIRST=0;


    static {
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document read = saxReader.read(resourceAsStream);
            Element rootElement = read.getRootElement();
            List<Element> list = rootElement.selectNodes("//bean");
            for (Element element : list) {
                String id = element.attributeValue("id");
                String classP = element.attributeValue("class");
                Class<?> aClass = Class.forName(classP);
                List<String> classList = beanClassMap.computeIfAbsent(aClass, k -> new ArrayList<>());
                classList.add(id);
                Object o = aClass.newInstance();
                beanMap.put(id,o);
            }
            List<Element> propertyList = rootElement.selectNodes("//property");
            for (Element element : propertyList) {
                String propertyName = element.attributeValue("name");
                String ref = element.attributeValue("ref");
                Element parent = element.getParent();
                String beanId = parent.attributeValue("id");
                Object targetBean = beanMap.get(beanId);
                for (Method method : targetBean.getClass().getMethods()) {
                    if (("set"+propertyName).equalsIgnoreCase(method.getName())){
                        method.invoke(targetBean,beanMap.get(ref));
                    }
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object getObject(String id){

        return beanMap.get(id);
    }

    public static Object getObject(Class clazz){
        List<String> classList = beanClassMap.get(clazz);
        if (CollectionUtils.isEmpty(classList)){
            throw new RuntimeException("bean not found exception");
        }
        if (classList.size()>1){
            throw new RuntimeException("no unique bean");
        }
        return beanMap.get(classList.get(FIRST));
    }
}
