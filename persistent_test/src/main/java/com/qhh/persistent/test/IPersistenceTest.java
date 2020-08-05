package com.qhh.persistent.test;



import com.qhh.io.Resources;
import com.qhh.persistent.dao.UserDao;
import com.qhh.persistent.pojo.User;
import com.qhh.sqlSession.SqlSession;
import com.qhh.sqlSession.SqlSessionFactory;
import com.qhh.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<User> users = userDao.queryAllUser();
        System.out.println(users);
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        User user1 = userDao.queryOneUser(user);
        System.out.println(user1);

    }



}
