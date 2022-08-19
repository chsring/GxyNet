package com.srwing.t_network.configs.http;

import com.srwing.t_network.configs.filter.data.IRespError;


/**
 * @author srwing
 * @fileName Function
 * @time 2022/6/17 2:36 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
@FunctionalInterface
public interface Function<T> {
    /**
     * 接口方法调用
     */
    void apply(IRespError respError);
}
