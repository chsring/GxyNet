package com.srwing.t_network.utils;

import android.content.Context;
import android.text.TextUtils;

import com.srwing.t_network.GxyNet;
import com.srwing.t_network.base.BaseView;
import com.srwing.t_network.callback.IFailure;
import com.srwing.t_network.callback.ISuccess;
import com.srwing.t_network.configs.ConfigKeys;
import com.srwing.t_network.configs.Configurator;
import com.srwing.t_network.http.IMethod;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * @author srwing
 * @fileName GxyNetBuilder
 * @time 2022/6/17 2:40 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc 
 */
public class GxyNetBuilder {

    private String mUrl = null;
    private final Map<String, Object> PARAMS = new WeakHashMap<>();
    private RequestBody mRequestBody = null;
    private Context mContext = null;
    private File mFile = null;
    private ISuccess mSuccess = null;
    private IFailure mFailure = null;
    private IMethod mMethod;
    private Class<?> mService;
    private LifecycleProvider mRxLifecycle;
    private LifecycleTransformer mRxLiefcycleTransformer;
    private BaseView mView;

    public GxyNetBuilder() {
    }

    public final GxyNetBuilder service(Class<?> service) {
        this.mService = service;
        return this;
    }

    public final GxyNetBuilder method(IMethod method) {
        this.mMethod = method;
        return this;
    }

    public final GxyNetBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final GxyNetBuilder params(Map<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final GxyNetBuilder params(String key, String value) {
        if (null != value)
            PARAMS.put(key, value);
        return this;
    }

    public final GxyNetBuilder params(String key, Object value) {
        if (null != value)
            PARAMS.put(key, value);
        return this;
    }

    public final GxyNetBuilder paramsValueObj(Map<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final GxyNetBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final GxyNetBuilder file(String filePath) {
        this.mFile = new File(filePath);
        return this;
    }

    public final GxyNetBuilder raw(String raw) {
        this.mRequestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final GxyNetBuilder context(Context context) {
        this.mContext = context;
        return this;
    }

    public final GxyNetBuilder bindLifeCycle(LifecycleProvider lifeCycle) {
        this.mRxLifecycle = lifeCycle;
        return this;
    }
    public final GxyNetBuilder bindLifeCycle(LifecycleTransformer lifecycleTransformer) {
        this.mRxLiefcycleTransformer = lifecycleTransformer;
        return this;
    }

    public final GxyNetBuilder view(BaseView view) {
        this.mView = view;
        this.mContext = view.getViewContext();
        this.mRxLifecycle = view;
        return this;
    }

    public final GxyNetBuilder success(ISuccess success) {
        this.mSuccess = success;
        return this;
    }

    public final GxyNetBuilder failure(IFailure failure) {
        this.mFailure = failure;
        return this;
    }

    public final GxyNet build() {
        HashMap<String, Object> globalParams = Configurator.getInstance().getConfiguration(ConfigKeys.NET_GLOBLE_PARAMS);
        if (null != globalParams) {
            PARAMS.putAll(globalParams);
        }
        String token = Configurator.getInstance().getConfiguration(ConfigKeys.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            PARAMS.put("token", token);
        }
        return new GxyNet(mUrl, mService,
                mMethod, PARAMS, mRequestBody,
                mContext,  mSuccess,
                mFailure, mRxLifecycle,mRxLiefcycleTransformer);
    }

}
