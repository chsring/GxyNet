package com.srwing.t_network.configs.http;

import java.util.WeakHashMap;

import io.reactivex.rxjava3.core.Observable;


/**
 * @author srwing
 * @fileName IMethod
 * @time 2022/6/17 2:36 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
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
