package com.qhh.demo.service.impl;


import com.qhh.demo.service.IDemoService;
import com.qhh.edu.mvcframework.annotations.QhhService;

@QhhService("demoService")
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        System.out.println("service 实现类中的name参数：" + name) ;
        return name;
    }
}
