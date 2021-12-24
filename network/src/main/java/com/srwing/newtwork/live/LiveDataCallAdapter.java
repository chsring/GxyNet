package com.srwing.newtwork.live;

import androidx.lifecycle.LiveData;

import com.srwing.newtwork.filter.RespFilterManager;
import com.srwing.newtwork.filter.data.RespData;
import com.srwing.newtwork.filter.data.RespEntity;
import com.srwing.newtwork.filter.data.RespError;
import com.srwing.newtwork.http.ErrorStatus;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A Retrofit adapterthat converts the Call into a LiveData of ApiResponse.
 *
 * @param <R>
 */
public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<RespData<R>>> {


    private static final int DEFAULT_SUCCESS_CODE = 0;

    private static final int OBSERVER_EXCEPTION = 700;

    private final Type responseType;

    LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<RespData<R>> adapt(Call<R> call) {

        return new LiveData<RespData<R>>() {

            final AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            processResponse(response);
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            processFailure(throwable);
                        }
                    });
                }
            }

            private void processFailure(Throwable throwable) {
                ErrorStatus es = ErrorStatus.getStatus(throwable);
                RespData<R> rRespData = new RespData<>();
                rRespData.respEntity = null;
                rRespData.respError = new RespError<R>(es.code, es.msg);
                rRespData.status = Status.ERROR;
                postValue(rRespData);
            }

            private void processResponse(Response<R> response) {

                RespData<R> successData = new RespData<>();
                successData.respEntity = new RespEntity<>(response.body());
                successData.respError = new RespError<>(DEFAULT_SUCCESS_CODE,"请求成功");
                try {
                    successData = RespFilterManager.execute(successData);
                } catch (Exception e) {
                    successData.respError = new RespError<>(OBSERVER_EXCEPTION, e.getMessage());
                    e.printStackTrace();
                }
                if (successData.errorCode() != DEFAULT_SUCCESS_CODE) {
                    successData.status = Status.ERROR;
                } else {
                    successData.status = Status.SUCCESS;
                }
                postValue(successData);
            }
        };
    }


}
