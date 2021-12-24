package com.srwing.newtwork.base;

import java.util.List;

/**
 * Description:
 * Created by srwing
 * Date: 7/1/2019
 * Email: surao@foryou56.com
 */
public interface BaseDataSource {

    interface LoadTasksCallback<T> {

        void onTasksLoaded(List<T> tasks);

        void onDataNotAvailable(int errorType, String message);
    }

    interface GetTaskCallback<T> {

        void onTaskLoaded(T task);

        void onDataNotAvailable(int code, String desc);
    }

}
