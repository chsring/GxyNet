package com.srwing.net;

import android.app.Application;

import com.srwing.t_network.GxyNet;
import com.srwing.t_network.interceptors.LogInterceptor;

/**
 * Description:
 * Created by small small su
 * Date: 2022/8/19
 * Email: surao@foryou56.com
 */
public class GxyNetApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GxyNet.init(this)
                .withApiHost("https://server6.19x19.com/")
                .withInterceptor(new LogInterceptor())
                .withLoggerAdapter() //设置LogAdapter
                .withDebugMode(true) //设置是否打印请求 日志
                .withNoProxy(false)
                .configure(); //配置生效
    }
}
