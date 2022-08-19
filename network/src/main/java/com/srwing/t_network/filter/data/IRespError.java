package com.srwing.t_network.filter.data;

/**
 * @author srwing
 * @fileName IRespError
 * @time 2022/6/17 2:32 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public interface IRespError<T> {

    int code();

    String errorMsg();

    void setCode(int code);

    void setErrorMsg(String errorMsg);

    T t();

    void setT(T t);


}
