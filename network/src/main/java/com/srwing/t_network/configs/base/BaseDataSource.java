package com.srwing.t_network.configs.base;

import java.util.List;

/**
 * Description:
 * Created by srwing
 * Date: 2022/6/17
 * Email: 694177407@qq.com
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
