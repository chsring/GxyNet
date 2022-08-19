package com.srwing.t_network.configs.utils;

import com.orhanobut.logger.Logger;
import com.srwing.t_network.configs.configs.Configurator;


/**
 * @author srwing
 * @fileName GxyLogger
 * @time 2022/6/17 2:40 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc 
 */
public class GxyLogger {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;

    //控制log等级
    private static final int LEVEL = VERBOSE;

    public static void v(String tag, String message) {
        if (LEVEL <= VERBOSE) {

            if (Configurator.isDebugMode()) {
                Logger.t(tag).v(message);
            }
        }
    }

    public static void d(String tag, Object message) {
        if (LEVEL <= DEBUG) {
            if (Configurator.isDebugMode()) {
                Logger.t(tag).d(message);
            }
        }
    }

    public static void d(Object message) {
        if (LEVEL <= DEBUG) {
            if (Configurator.isDebugMode()) {
                Logger.d(message);
            }
        }
    }

    public static void i(String tag, String message) {
        if (LEVEL <= INFO) {
            if (Configurator.isDebugMode()) {
                Logger.t(tag).i(message);
            }
        }
    }

    public static void w(String tag, String message) {
        if (LEVEL <= WARN) {
            if (Configurator.isDebugMode()) {
                Logger.t(tag).w(message);
            }
        }
    }

    public static void json(String tag, String message) {
        if (LEVEL <= WARN) {
            if (Configurator.isDebugMode()) {
                Logger.t(tag).json(message);
            }
        }
    }

    public static void e(String tag, String message) {
        if (LEVEL <= ERROR) {
            if (Configurator.isDebugMode()) {
                Logger.t(tag).e(message);
            }
        }
    }


}
