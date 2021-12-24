package com.srwing.newtwork.callback;

/**
 * Description:
 * Created by srwing
 * Date: 29/11/2018
 * Email: surao@foryou56.com
 */
public interface IFailure {

    /**
     * @param code request error code
     * @param desc request error description
     */
    void onFailure(int code, String desc);

}
