package com.qhh.io;

import java.io.InputStream;

/**
 * @Author qinhaohao
 * @Date 2020-05-22 18:34
 **/
public class Resources {

    public static InputStream getResourceAsSteam(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
