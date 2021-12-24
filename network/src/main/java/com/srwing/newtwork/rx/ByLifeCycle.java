package com.srwing.newtwork.rx;


import com.trello.rxlifecycle4.LifecycleTransformer;

/**
 * Description:
 * Created by srwing
 * Date: 23/11/2018
 * Email: surao@foryou56.com
 */
@FunctionalInterface
public interface ByLifeCycle {
    /**
     * 绑定生命周期
     */
    <T> LifecycleTransformer<T> bindToLife();
}
