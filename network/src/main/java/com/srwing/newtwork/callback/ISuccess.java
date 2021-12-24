package com.srwing.newtwork.callback;

/**
 * Description:
 * Created by srwing
 * Date: 29/11/2018
 * Email: surao@foryou56.com
 */
public interface ISuccess<T> {

    /**
     * request success callback method
     *
     * @param response request success data
     */
    void onSuccess(T response);

}
