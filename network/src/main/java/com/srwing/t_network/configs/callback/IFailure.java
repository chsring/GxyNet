package com.srwing.t_network.configs.callback;

/**
 * @author srwing
 * @fileName IFailure
 * @time 2022/6/17 2:30 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public interface IFailure {

    /**
     * @param code request error code
     * @param desc request error description
     */
    void onFailure(int code, String desc);

}
