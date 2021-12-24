package com.srwing.newtwork.filter.data;

import java.io.Serializable;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
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
        t = (T) o;
    }

}
