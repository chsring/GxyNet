package com.srwing.newtwork.http;

import java.util.WeakHashMap;

import io.reactivex.rxjava3.core.Observable;


/**
 * Description:
 * Created by srwing
 * Date: 29/11/2018
 * Email: surao@foryou56.com
 */
@FunctionalInterface
public interface IMethod<X> {
    /**
     * 接口方法调用
     *
     * @param service 具体的Service类
     * @param params  request params
     * @return 返回的Observable<>对象W
     */
    Observable ob(X service, WeakHashMap<String, Object> params);
}
