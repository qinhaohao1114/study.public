package com.qhh.demo.controller;

import com.qhh.demo.service.IDemoService;
import com.qhh.edu.mvcframework.annotations.QhhAutowird;
import com.qhh.edu.mvcframework.annotations.QhhController;
import com.qhh.edu.mvcframework.annotations.QhhRequestMapping;
import com.qhh.edu.mvcframework.annotations.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@QhhController
@QhhRequestMapping("/demo")
public class DemoController {


    @QhhAutowird
    private IDemoService demoService;


    /**
     * URL: /demo/query?name=lisi
     * @param request
     * @param response
     * @param name
     * @return
     */
    @QhhRequestMapping("/query")
    @Security(value = "zhangsan,wangwu",pattern = ".*runoob.*")
    public String query(HttpServletRequest request, HttpServletResponse response,String name) {
        return demoService.get(name);
    }
}
