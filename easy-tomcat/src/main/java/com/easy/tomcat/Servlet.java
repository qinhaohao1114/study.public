package com.easy.tomcat;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 6:06 下午
 **/
public interface Servlet {

    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request,Response response) throws Exception;
}
