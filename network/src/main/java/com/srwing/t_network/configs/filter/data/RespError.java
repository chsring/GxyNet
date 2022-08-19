package com.srwing.t_network.configs.filter.data;

import java.io.Serializable;

/**
 * @author srwing
 * @fileName RespError
 * @time 2022/6/17 2:33 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public class RespError<T> implements IRespError<T>, Serializable {

    private int code;
    private String errorMsg;
    private T t;

    public RespError(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public RespError(int code, String errorMsg, T t) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.t = t;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String errorMsg() {
        return errorMsg;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public T t() {
        return t;
    }

    @Override
    public void setT(T o) {
        this.t = o;
    }


}
