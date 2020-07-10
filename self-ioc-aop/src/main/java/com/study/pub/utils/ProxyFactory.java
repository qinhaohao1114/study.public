package com.study.pub.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author qinhaohao
 * @Date 2020-05-30 13:51
 **/
public class ProxyFactory {

    private TransactionManager transactionManager;


    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    public Object getProxy(Object target){

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(), (proxy, method, args) -> {
            Object result;
            try {
                transactionManager.beginTransaction();
                result=method.invoke(target,args);
                transactionManager.commitTransaction();
            }catch (Exception e){
                transactionManager.rollbackTransaction();
                throw e.getCause();
            }
            return result;
        });
    }

    /**
     * 使用cglib动态代理生成代理对象
     * @param obj 委托对象
     * @return
     */
    public Object getCglibProxy(Object obj) {
        return  Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object result = null;
                try{
                    // 开启事务(关闭事务的自动提交)
                    transactionManager.beginTransaction();

                    result = method.invoke(obj,objects);

                    // 提交事务

                    transactionManager.commitTransaction();
                }catch (Exception e) {
                    e.printStackTrace();
                    // 回滚事务
                    transactionManager.rollbackTransaction();

                    // 抛出异常便于上层servlet捕获
                    throw e;

                }
                return result;
            }
        });
    }
}
