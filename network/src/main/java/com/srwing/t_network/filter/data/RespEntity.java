package com.srwing.t_network.filter.data;

import java.io.Serializable;

/**
 * @author srwing
 * @fileName RespEntity
 * @time 2022/6/17 2:33 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public class RespEntity<T> implements IRespEntity<T>, Serializable {

    private T t;

    public RespEntity(T t) {
        this.t = t;
    }

    @Override
    public T entity() {
        return t;
    }

    @Override
    public void setEntity(T o) {
        t = o;
    }

}
