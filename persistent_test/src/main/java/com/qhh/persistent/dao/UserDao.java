package com.qhh.persistent.dao;

import com.qhh.persistent.pojo.User;
import org.testng.collections.Lists;

import java.util.List;

/**
 * @Author qinhaohao
 * @Date 2020-05-23 15:32
 **/
public interface UserDao {

    List<User> queryAllUser();

    User queryOneUser(User user);
}
