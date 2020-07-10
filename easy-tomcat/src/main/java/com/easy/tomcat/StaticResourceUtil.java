package com.easy.tomcat;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 5:50 下午
 **/
public class StaticResourceUtil {
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        System.out.println("absolutePath=====>"+absolutePath);
        return absolutePath.replaceAll("\\\\","/")+path;
    }

    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws Exception {
        int count=0;
        while (count==0){
            count=inputStream.available();
        }
        int resourceSize = count;
        outputStream.write(HttpProtocolUtil.getHttpHeader200(count).getBytes());

        long written=0;
        int byteSize=1024;
        byte[] bytes = new byte[byteSize];
        while (written<resourceSize){
            if (written+byteSize>resourceSize){
                byteSize= (int) (resourceSize-written);
                bytes=new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written+=byteSize;
        }

    }
}
