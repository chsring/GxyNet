package com.srwing.t_network.configs.base;
/**
 * @author srwing
 * @fileName GetTaskSucc
 * @time 2022/6/17 2:31 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public interface GetTaskSucc<T> {
    void onTaskLoaded(T task);
}
