package com.srwing.t_network;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.srwing.t_network.callback.IFailure;
import com.srwing.t_network.callback.ISuccess;
import com.srwing.t_network.configs.ConfigKeys;
import com.srwing.t_network.configs.Configurator;
import com.srwing.t_network.http.HttpCreator;
import com.srwing.t_network.http.IMethod;
import com.srwing.t_network.rx.GxyObserver;
import com.srwing.t_network.rx.SwitchSchedulers;
import com.srwing.t_network.utils.GxyNetBuilder;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;

import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;

/**
 * Description:
 * Created by srwing
 * Date: 2022/6/17
 * Email: 694177407@qq.com
 */
public class GxyNet {

    public static final String TAG = GxyNet.class.getSimpleName();

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    private final RequestBody BODY;
    private final Context CONTEXT;
    private final ISuccess<Object> SUCCESS;
    private final IFailure FAILURE;
    private final IMethod NETMETHOD;
    private final Class<?> SERVICE;
    private final LifecycleProvider LIFE_CYCLE;
    private final LifecycleTransformer LIFE_TRANSFORMER;

    public GxyNet(String url,
                  Class<?> service,
                  IMethod method,
                  Map<String, Object> params,
                  RequestBody body,
                  Context context,
                  ISuccess<Object> success,
                  IFailure failure,
                  LifecycleProvider lifecycle,
                  LifecycleTransformer lifecycleTransformer

    ) {
        this.URL = url;
        this.SERVICE = service;
        this.NETMETHOD = method;
        if (!PARAMS.isEmpty()) {
            PARAMS.clear();
        }
        PARAMS.putAll(params);
        this.BODY = body;
        this.CONTEXT = context;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.LIFE_CYCLE = lifecycle;
        this.LIFE_TRANSFORMER = lifecycleTransformer;
    }

    public static GxyNetBuilder builder() {
        return new GxyNetBuilder();
    }


    public static Configurator init(Context context) {
        Configurator.getInstance().getFoYoConfigs().put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return GxyNet.getConfigurator();
    }

    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    /**
     * 可用来设置 全局变量
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
     */
    public <T> Observable<T> request() throws Exception {

        Observable<T> observable = null;
        if (null == NETMETHOD) {
            throw new Exception("net method can not be null");
        }
        Object service;
        if (null != SERVICE) {
            if (TextUtils.isEmpty(URL)) {
                service = HttpCreator.getService(SERVICE);
            } else {
                service = HttpCreator.getService(SERVICE, URL);
            }
        } else {
            throw new Exception("Attention : service method parameters can not be null");
        }
//        if (PARAMS.isEmpty()) {
//            throw new Exception("params can not be null");
//        }
        observable = NETMETHOD.ob(service, PARAMS);
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
    public <T> void excute() {
        try {
            Observable<T> o = request();
            if (null != o) {
                execute(o, new CallListener<T>() {
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
     */
    private <T> void execute(Observable<T> observable, final CallListener<T> callBack) {
        Observable<T> tObservable = observable.compose(new SwitchSchedulers<T>().applySchedulers());
        if (null != LIFE_CYCLE) {
            tObservable = tObservable.compose(LIFE_CYCLE.bindToLifecycle());
        }else if(null!= LIFE_TRANSFORMER){
            tObservable = tObservable.compose(LIFE_TRANSFORMER);
        }
        tObservable.subscribe(new GxyObserver<T>() {
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

