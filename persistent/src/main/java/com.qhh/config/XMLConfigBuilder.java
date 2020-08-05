package com.qhh.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.qhh.io.Resources;
import com.qhh.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.qhh.pojo.Configuration;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author qinhaohao
 * @Date 2020-05-23 14:57
 **/
public class XMLConfigBuilder {


    public Configuration parseConfig(Configuration configuration, InputStream in) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties=new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.put(name,value);
        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);
        List<Element> mappers = rootElement.selectNodes("//mapper");
        for (Element mapper : mappers) {
            String mapperPath = mapper.attributeValue("resource");
            InputStream resourceAsSteam = Resources.getResourceAsSteam(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder();
            Map<String, MappedStatement> mappedStatementMap=xmlMapperBuilder.parse(resourceAsSteam);
            configuration.getMappedStatementMap().putAll(mappedStatementMap);
        }
        return configuration;
    }
}
