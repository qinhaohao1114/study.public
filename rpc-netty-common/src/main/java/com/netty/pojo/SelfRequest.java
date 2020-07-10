package com.netty.pojo;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 10:29 上午
 **/
public class SelfRequest implements Serializable {

    private String className;

    private String methodName;

    LinkedHashMap<String,Object> params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public LinkedHashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(LinkedHashMap<String, Object> params) {
        this.params = params;
    }
}
