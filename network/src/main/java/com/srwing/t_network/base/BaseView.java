package com.srwing.t_network.base;

import android.content.Context;

import com.trello.rxlifecycle4.LifecycleProvider;

/**
 * Description:
 * Created by srwing
 * Date: 2022/6/17
 * Email: 694177407@qq.com
 */
public interface BaseView extends LifecycleProvider {

    Context getViewContext();

}
