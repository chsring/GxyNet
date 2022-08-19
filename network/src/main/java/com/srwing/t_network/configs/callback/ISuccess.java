package com.srwing.t_network.configs.callback;

/**
 * @author srwing
 * @fileName ISuccess
 * @time 2022/6/17 2:31 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public interface ISuccess<T> {

    /**
     * request success callback method
     *
     * @param response request success data
     */
    void onSuccess(T response);

}
