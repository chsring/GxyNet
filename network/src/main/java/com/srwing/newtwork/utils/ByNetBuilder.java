package com.srwing.newtwork.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import com.srwing.newtwork.http.IMethod;
import com.srwing.newtwork.callback.IFailure;
import com.srwing.newtwork.callback.ISuccess;
import com.srwing.newtwork.configs.ConfigKeys;
import com.srwing.newtwork.configs.Configurator;
import com.srwing.newtwork.ByNet;
import com.srwing.newtwork.base.BaseView;
import com.srwing.newtwork.rx.ByLifeCycle;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Description:
 * Created by srwing
 * Date: 7/1/2019
 * Email: surao@foryou56.com
 */
public class ByNetBuilder {

    private String mUrl = null;
    private final Map<String, Object> PARAMS = new WeakHashMap<>();
    private RequestBody mRequestBody = null;
    private Context mContext = null;
    private File mFile = null;
    private ISuccess mSuccess = null;
    private IFailure mFailure = null;
    private IMethod mMethod;
    private Class<?> mService;
    private ByLifeCycle mFoYoRxLifecycle;
    private BaseView mView;

    public ByNetBuilder() {
    }

    public final ByNetBuilder service(Class<?> service) {
        this.mService = service;
        return this;
    }

    public final ByNetBuilder method(IMethod method) {
        this.mMethod = method;
        return this;
    }

    public final ByNetBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final ByNetBuilder params(Map<String, String> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final ByNetBuilder params(String key, String value) {
        if (null != value)
            PARAMS.put(key, value);
        return this;
    }

    public final ByNetBuilder params(String key, Object value) {
        if (null != value)
            PARAMS.put(key, value);
        return this;
    }

    public final ByNetBuilder paramsValueObj(Map<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final ByNetBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final ByNetBuilder file(String filePath) {
        this.mFile = new File(filePath);
        return this;
    }

    public final ByNetBuilder raw(String raw) {
        this.mRequestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final ByNetBuilder context(Context context) {
        this.mContext = context;
        return this;
    }

    public final ByNetBuilder bindLifeCycle(ByLifeCycle lifeCycle) {
        this.mFoYoRxLifecycle = lifeCycle;
        return this;
    }

    public final ByNetBuilder view(BaseView view) {
        this.mView = view;
        this.mContext = view.getViewContext();
        this.mFoYoRxLifecycle = view;
        return this;
    }

    public final ByNetBuilder success(ISuccess success) {
        this.mSuccess = success;
        return this;
    }

    public final ByNetBuilder failure(IFailure failure) {
        this.mFailure = failure;
        return this;
    }

    public final ByNet build() {
        HashMap<String, Object> globalParams = Configurator.getInstance().getConfiguration(ConfigKeys.NET_GLOBLE_PARAMS);
        if (null != globalParams) {
            PARAMS.putAll(globalParams);
        }
        String token = Configurator.getInstance().getConfiguration(ConfigKeys.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            PARAMS.put("token", token);
        }
        return new ByNet(mUrl, mService,
                mMethod, PARAMS, mRequestBody,
                mContext,  mSuccess,
                mFailure, mFoYoRxLifecycle);
    }

}
