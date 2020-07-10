package com.netty;

import com.netty.pojo.Dog;
import com.netty.pojo.User;
import com.netty.proxy.RpsConsumer;
import com.netty.service.UserService;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 11:13 上午
 **/
public class ClientBootStrap {

    public static void main(String[] args) throws InterruptedException {
        UserService userService = (UserService) new RpsConsumer().createProxy(UserService.class);
        while (true){
            Thread.sleep(2000);
//            System.out.println(userService.sayHello("你好啊！"));
            User user = new User();
            user.setName("泡泡");
            Dog dog = new Dog();
            dog.setDogName("大狗子");
            user.setDog(dog);
            System.out.println(userService.findUser(user));
        }
    }
}
