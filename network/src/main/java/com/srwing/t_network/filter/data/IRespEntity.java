package com.srwing.t_network.filter.data;

/**
 * @author srwing
 * @fileName IRespEntity
 * @time 2022/6/17 2:32 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public interface IRespEntity<T> {

    T entity();

    void setEntity(T t);

}
