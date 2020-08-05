package com.qhh.config;

import com.qhh.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qinhaohao
 * @Date 2020-05-23 15:14
 **/
public class XMLMapperBuilder {
    public Map<String, MappedStatement> parse(InputStream in) throws DocumentException {
        HashMap<String, MappedStatement> result = new HashMap<>();
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> list = rootElement.selectNodes("//query");
        for (Element element : list) {
            MappedStatement mappedStatement = new MappedStatement();
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sqlText = element.getTextTrim();
            String key=namespace+"."+id;
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setSql(sqlText);
            result.put(key,mappedStatement);
        }
        return result;
    }
}
