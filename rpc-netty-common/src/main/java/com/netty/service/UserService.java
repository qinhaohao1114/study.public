package com.netty.service;

import com.netty.pojo.User;

public interface UserService {

       String sayHello(String word);

       User findUser(User user);


}
