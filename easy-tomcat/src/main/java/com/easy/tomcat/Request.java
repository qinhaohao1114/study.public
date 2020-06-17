package com.easy.tomcat;

import java.io.InputStream;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 5:22 下午
 **/
public class Request {
    private String method; // 请求方式，比如GET/POST
    private String url;  // 例如 /,/index.html

    private InputStream inputStream;  // 输入流，其他属性从输入流中解析出来

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request(InputStream inputStream) throws Exception{
        this.inputStream = inputStream;
        int count = 0;
        while (count==0){
            count=inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String inputStr = new String(bytes);
        String firstLine = inputStr.split("\\n")[0];
        String[] lines = firstLine.split(" ");
        this.method=lines[0];
        this.url=lines[1];
        System.out.println("=====>>method:" + method);
        System.out.println("=====>>url:" + url);
    }
}
