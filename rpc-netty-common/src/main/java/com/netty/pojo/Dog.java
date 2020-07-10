package com.netty.pojo;

import java.io.Serializable;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 1:58 下午
 **/
public class Dog implements Serializable {

    private String dogName;

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "dogName='" + dogName + '\'' +
                '}';
    }
}
