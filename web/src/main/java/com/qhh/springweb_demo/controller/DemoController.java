package com.qhh.springweb_demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

@RestController
public class DemoController {

      @RequestMapping("/demo")
      public String demo(){
          return "hello spring Boot";
      }
}