package com.study.pub.service;

public interface TransferService {

   void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
