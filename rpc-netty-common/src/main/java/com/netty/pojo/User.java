package com.netty.pojo;

import java.io.Serializable;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 11:49 上午
 **/
public class User implements Serializable {

    private String name;

    private Dog dog;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", dog=" + dog +
                '}';
    }
}
