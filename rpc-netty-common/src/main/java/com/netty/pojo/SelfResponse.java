package com.netty.pojo;

import java.io.Serializable;

/**
 * @Author qinhaohao
 * @Date 2020/7/10 11:48 上午
 **/
public class SelfResponse implements Serializable {

    private Integer code;

    private Object result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
