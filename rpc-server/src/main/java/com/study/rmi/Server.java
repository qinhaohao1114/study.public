package com.study.rmi;

import com.study.rmi.service.Hello;
import com.study.rmi.service.impl.HelloImpl;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 10:12 上午
 * 两个接口的所属包名必须一致，否则会报ClassNotFoundException错误
 **/
public class Server {

    public static void main(String[] args) {
        try {
            Hello hello=new HelloImpl();
            LocateRegistry.createRegistry(8080);
            Naming.bind("//127.0.0.1:8080/zm",hello);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
