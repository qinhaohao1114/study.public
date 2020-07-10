package com.study.rmi.service;


import com.study.rmi.pojo.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {

     String sayHello(User user) throws RemoteException;
}
