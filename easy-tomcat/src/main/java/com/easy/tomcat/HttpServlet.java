package com.easy.tomcat;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 6:08 下午
 **/
public abstract class HttpServlet implements Servlet{

    public abstract void deGet(Request request,Response response);

    public abstract void dePost(Request request,Response response);

    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equals(request.getMethod())){
            deGet(request,response);
        }else {
            dePost(request,response);
        }
    }

}
