package com.srwing.newtwork;

import android.content.Context;
import android.os.Handler;
import java.util.Map;
import java.util.WeakHashMap;
import com.srwing.newtwork.http.IMethod;
import com.srwing.newtwork.callback.IFailure;
import com.srwing.newtwork.callback.ISuccess;
import com.srwing.newtwork.configs.ConfigKeys;
import com.srwing.newtwork.configs.Configurator;
import com.srwing.newtwork.utils.ByNetBuilder;
import com.srwing.newtwork.http.HttpCreator;
import com.srwing.newtwork.rx.ByLifeCycle;
import com.srwing.newtwork.rx.ByObserver;
import com.srwing.newtwork.rx.SwitchSchedulers;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;

/**
 * Description: By tools；Sr & By
 * Created by Srwing
 * Date: 29/11/2021
 * Email: 694177407@qq.com
 */
public class ByNet {

    public static final String TAG = ByNet.class.getSimpleName();

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    private final RequestBody BODY;
    private final Context CONTEXT;
    private final ISuccess<Object> SUCCESS;
    private final IFailure FAILURE;
    private final IMethod FOYONETMETHOD;
    private final Class<?> SERVICE;
    private final ByLifeCycle LIFE_CYCLE;

    public ByNet(String url,
                 Class<?> service,
                 IMethod method,
                 Map<String, Object> params,
                 RequestBody body,
                 Context context,
                 ISuccess<Object> success,
                 IFailure failure,
                 ByLifeCycle lifecycle
    ) {
        this.URL = url;
        this.SERVICE = service;
        this.FOYONETMETHOD = method;
        if (!PARAMS.isEmpty()) {
            PARAMS.clear();
        }
        PARAMS.putAll(params);
        this.BODY = body;
        this.CONTEXT = context;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.LIFE_CYCLE = lifecycle;
    }

    public static ByNetBuilder builder() {
        return new ByNetBuilder();
    }


    public static Configurator init(Context context) {
        Configurator.getInstance().getFoYoConfigs().put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return ByNet.getConfigurator();
    }

    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    /**
     * 可用来设置 全局变量
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getConfiguration(Object key) {
        return Configurator.getInstance().getConfiguration(key);
    }

    public static Context getApp() {
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigKeys.HANDLER);
    }

    /**
     * 构造请求Observable 可在项目中RxJava 串行使用
     * @param <T>
     * @return
     */
    public <T> Observable<T> request() throws Exception {

        Observable observable = null;
        if (null == FOYONETMETHOD) {
            throw new Exception("net method can not be null");
        }
        Object service;
        if (null != SERVICE) {
            service = HttpCreator.getService(SERVICE);
        } else {
            throw new Exception("Attention : service method parameters can not be null");
        }
        if (PARAMS.isEmpty()) {
            throw new Exception("params can not be null");
        }
        observable = FOYONETMETHOD.ob(service, PARAMS);
        if (null == observable) {
            throw new Exception("observable can not be null");
        }
        return observable;
    }


    private interface CallListener<T> {

        void onSucc(T data);

        void onFail(int code, String desc);
    }

    /**
     * 调用次方法 执行具体的请求操作
     */
    public void excute() {
        try {
            Observable o = request();
            if (null != o) {
                execute(o, new CallListener() {
                    @Override
                    public void onSucc(Object data) {
                        if (null != SUCCESS) {
                            SUCCESS.onSuccess(data);
                        }
                    }

                    @Override
                    public void onFail(int code, String desc) {
                        if (null != FAILURE) {
                            FAILURE.onFailure(code, desc);
                        }
                    }
                });
            } else {
                throw new Exception("Observable get failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置请求的具体回调监听
     *
     * @param observable
     * @param <T>
     */
    private <T> void execute(Observable<T> observable, final CallListener callBack) {
        Observable<T> tObservable = observable.compose(new SwitchSchedulers<T>().applySchedulers());
        if (null != LIFE_CYCLE) {
            tObservable = tObservable.compose(LIFE_CYCLE.bindToLife());
        }
        tObservable.subscribe(new ByObserver<T>() {
            @Override
            public void onSuccess(T data) {
                callBack.onSucc(data);
            }

            @Override
            public void onFailure(int code, String desc) {
                callBack.onFail(code, desc);
            }
        });
    }


}
