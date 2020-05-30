package com.study.pub.utils;

import com.study.pub.pojo.Result;

import java.lang.reflect.InvocationHandler;
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
}
