package com.study.rmi.pojo;

import java.io.Serializable;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 10:19 上午
 **/
public class User implements Serializable {

   private String name;

   private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
