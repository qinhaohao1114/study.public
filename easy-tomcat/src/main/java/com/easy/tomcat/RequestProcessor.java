package com.easy.tomcat;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 6:30 下午
 **/
public class RequestProcessor extends Thread{

    private Socket socket;

    private Map<String,HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    public RequestProcessor() {

    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Request request = new Request(inputStream);
            Response response = new Response(outputStream);
            if (request.getUrl().endsWith(".ico")){
                response.output(HttpProtocolUtil.getHttpHeader200(0));
                return;
            }
            if (request.getUrl().endsWith(".html")){
                response.outputHtml(request.getUrl());
            }else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                if (httpServlet==null){
                    response.output(HttpProtocolUtil.getHttpHeader404());
                    return;
                }
                httpServlet.service(request,response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
