package com.srwing.newtwork.http;

import com.srwing.newtwork.filter.data.IRespError;


/**
 * Description:
 * Created by srwing
 * Date: 29/11/2018
 * Email: surao@foryou56.com
 */
@FunctionalInterface
public interface Function<T> {
    /**
     * 接口方法调用
     */
    void apply(IRespError respError);
}
