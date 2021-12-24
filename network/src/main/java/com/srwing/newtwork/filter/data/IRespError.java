package com.srwing.newtwork.filter.data;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
 */
public interface IRespError<T> {

    int code();

    String errorMsg();

    void setCode(int code);

    void setErrorMsg(String errorMsg);

    T t();

    void setT(T t);


}
