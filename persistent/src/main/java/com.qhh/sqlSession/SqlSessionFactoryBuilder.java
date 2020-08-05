package com.qhh.sqlSession;

import com.qhh.config.XMLConfigBuilder;
import com.qhh.config.XMLConfigBuilder;
import com.qhh.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @Author qinhaohao
 * @Date 2020-05-23 13:48
 **/
public class SqlSessionFactoryBuilder {


    public SqlSessionFactory build(InputStream in) throws Exception {
    // 第一：使用dom4j解析配置文件，将解析出来的内容封装到Configuration中
        XMLConfigBuilder xmlConfigBuilder=new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(new Configuration(),in);

        // 第二：创建sqlSessionFactory对象：工厂类：生产sqlSession:会话对象
        // 第二：创建sqlSessionFactory对象：工厂类：生产sqlSession:会话对象

        return new DefaultSqlSessionFactory(configuration);
    }
}
