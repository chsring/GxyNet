package com.srwing.t_network.live;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.srwing.t_network.filter.data.RespData;


/**
 * Description:
 * Created by surao
 * Date:2019-06-24 10:27
 * Email:surao@foryou56.com
 */
public abstract class HttpCall<ResultType> {

    private final MediatorLiveData<RespData<ResultType>> result = new MediatorLiveData<>();

    public HttpCall() {

        LiveData<RespData<ResultType>> respDataLiveData = liveMethod();
        result.addSource(respDataLiveData, result::setValue);
    }

    public LiveData<RespData<ResultType>> asLiveData() {
        return result;
    }

    public abstract LiveData<RespData<ResultType>> liveMethod();


}
