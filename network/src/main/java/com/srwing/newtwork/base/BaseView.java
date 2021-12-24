package com.srwing.newtwork.base;

import android.content.Context;

import com.srwing.newtwork.rx.ByLifeCycle;

/**
 * Description:
 * Created by srwing
 * Date: 7/1/2019
 * Email: surao@foryou56.com
 */
public interface BaseView extends ByLifeCycle {

    Context getViewContext();

}
