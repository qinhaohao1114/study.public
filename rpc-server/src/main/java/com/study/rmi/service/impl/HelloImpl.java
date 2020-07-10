package com.study.rmi.service.impl;

import com.study.rmi.pojo.User;
import com.study.rmi.service.Hello;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 10:22 上午
 **/
public class HelloImpl extends UnicastRemoteObject implements Hello {

    public HelloImpl() throws RemoteException {
    }

    public String sayHello(User user) throws RemoteException {
        System.out.println("this is server,hello:"+user.getName());
        return "success";
    }
}
