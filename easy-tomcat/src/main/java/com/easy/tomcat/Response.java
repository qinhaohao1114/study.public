package com.easy.tomcat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 5:22 下午
 **/
public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response() {
    }

    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());;
    }

    public void outputHtml(String path) throws Exception {
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);
        File file = new File(absoluteResourcePath);
        if (file.exists()&&file.isFile()){
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else {
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }

}
