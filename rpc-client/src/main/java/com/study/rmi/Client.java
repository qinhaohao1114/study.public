package com.study.rmi;

import com.study.rmi.pojo.User;
import com.study.rmi.service.Hello;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 10:42 上午
 **/
public class Client {

    public static void main(String[] args) {
        try {
            Hello hello = (Hello) Naming.lookup("//127.0.0.1:8080/zm");
            User user = new User();
            user.setName("james");
            System.out.println(hello.sayHello(user));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
