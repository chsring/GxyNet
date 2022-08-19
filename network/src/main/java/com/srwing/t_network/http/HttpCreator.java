package com.srwing.t_network.http;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.srwing.t_network.GxyNet;
import com.srwing.t_network.configs.ConfigKeys;
import com.srwing.t_network.configs.Configurator;
import com.srwing.t_network.live.LiveDataCallAdapterFactory;
import com.srwing.t_network.utils.ResourceTools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author srwing
 * @fileName HttpCreator
 * @time 2022/6/17 2:32 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public class HttpCreator {

    /**
     * 参数容器
     */
    private static final class ParamsHolder {
        private static final WeakHashMap<String, String> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, String> getParams() {
        return ParamsHolder.PARAMS;
    }

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {

        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS = GxyNet.getConfiguration(ConfigKeys.INTERCEPTOR);

        private static OkHttpClient.Builder addInterceptor() {
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }
            setNoProxy();
            //BUILDER.sslSocketFactory(Objects.requireNonNull(getSSLSocketFactory(trustedCertificatesInputStream())));
            return BUILDER;
        }

        private static void setNoProxy() {
            Object proxyObject = Configurator.getInstance().getConfiguration(ConfigKeys.NET_NO_PROXY);
            if (null != proxyObject) {
                boolean noProxy = (boolean) proxyObject;
                if (noProxy) {
                    BUILDER.proxy(java.net.Proxy.NO_PROXY);
                }
            }
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();

    }

    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = GxyNet.getConfiguration(ConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
//              .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GxyNet.getConfiguration(ConfigKeys.NET_FACTORY) == null ? GsonConverterFactory.create() : GxyNet.getConfiguration(ConfigKeys.NET_FACTORY))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    private static final ConcurrentMap<Object, Object> map = new ConcurrentHashMap<>();

    /**
     * Get SSLSocketFactory
     *
     * @param certificates ssl key
     * @return
     */
    private static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static InputStream trustedCertificatesInputStream() {
        String cer = GxyNet.getConfiguration(ConfigKeys.HTTPS_CER);
        String comodoRsaCertificationAuthority = ResourceTools.readAssets2String(GxyNet.getApp(), cer);
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .inputStream();
    }


    /**
     * 多业务Service 单例模式
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> type) {

        //从map中取出对象的相对应的实例 为减少对map的操作，在此处使用局部变量ob --- 符合优化性能要求
        Object ob = map.get(type);
        try {
            //对该对象的实例进行null判断
            if (ob == null) {
                //为使用安全的map及其操作 设置同步锁
                synchronized (map) {
                    //创建此 Class 对象所表示的类的一个新实例。
                    //如同用一个带有一个空参数列表的 new 表达式实例化该类。
                    //如果该类尚未初始化，则初始化这个类。
                    ob = RetrofitHolder.RETROFIT_CLIENT.create(type);
//                    ob = createWrapperService(RetrofitHolder.RETROFIT_CLIENT, type);
                    // 将对象类型和创建的新实例放进map
                    map.put(type, ob);
                }
            }
            //是type.newInstance()产生的两个异常处理
            //IllegalAccessException - 如果该类或其 null 构造方法是不可访问的。
            //InstantiationException - 如果此 Class 表示一个抽象类、接口、数组类、
            //基本类型或 void； 或者该类没有 null 构造方法； 或者由于其他某种原因导致实例化失败
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将从map中取出的对象的实例化 转换成泛型参数 返回
        return (T) ob;
    }


    /**
     * 白名单的URL
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> service, String baseUrl) {
        //从map中取出对象的相对应的实例 为减少对map的操作，在此处使用局部变量ob --- 符合优化性能要求
        Object key = service;
        if (!TextUtils.isEmpty(baseUrl)) {
            key = service.getSimpleName() + baseUrl;
        }
        Object ob = map.get(key);
        try {
            //对该对象的实例进行null判断
            if (ob == null) {
                //为使用安全的map及其操作 设置同步锁
                synchronized (map) {
                    //创建此 Class 对象所表示的类的一个新实例。
                    //如同用一个带有一个空参数列表的 new 表达式实例化该类。
                    //如果该类尚未初始化，则初始化这个类。
                    ob = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(OKHttpHolder.OK_HTTP_CLIENT)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .build().create(service);
//                    ob = createWrapperService(RetrofitHolder.RETROFIT_CLIENT, type);
                    // 将对象类型和创建的新实例放进map
                    map.put(key, ob);
                }
            }
            //是type.newInstance()产生的两个异常处理
            //IllegalAccessException - 如果该类或其 null 构造方法是不可访问的。
            //InstantiationException - 如果此 Class 表示一个抽象类、接口、数组类、
            //基本类型或 void； 或者该类没有 null 构造方法； 或者由于其他某种原因导致实例化失败
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将从map中取出的对象的实例化 转换成泛型参数 返回
        return (T) ob;
    }

    /**
     * 创建包装Service 提升启动速度
     *
     * @param retrofit
     * @param service
     * @param <T>
     * @return
     */
    private static <T> T createWrapperService(final Retrofit retrofit, final Class<T> service) {

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(service.getClassLoader(),

                new Class<?>[]{service}, new InvocationHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("CheckResult")
                    @Override
                    public Object invoke(Object proxy, final Method method, @Nullable final Object[] args) throws Throwable {

                        if (method.getReturnType() == Observable.class) {
                            // 如果方法返回值是 Observable 的话，则包一层再返回
                            return Observable.fromCallable(new Callable<ObservableSource<?>>() {
                                @Override
                                public ObservableSource<?> call() throws Exception {

                                    final T service = getRetrofitService();
                                    // 执行真正的 Retrofit 动态代理的方法
                                    return ((Observable) getRetrofitMethod(service, method).invoke(service, args))
                                            .subscribeOn(Schedulers.io());
                                }
                            }).subscribeOn(Schedulers.single());
                        }
                        // 返回值不是 Observable 的话不处理
                        final T service = getRetrofitService();
                        return getRetrofitMethod(service, method).invoke(service, args);
                    }

                    //private volatile T retrofitService;
//                    private T getRetrofitService() {
//                        if (retrofitService != null) {
//                            return retrofitService;
//                        }
//                        synchronized (this) {
//                            if (retrofitService != null) {
//                                return retrofitService;
//                            }
//                            retrofitService = retrofit.create(service);
//                        }
//                        retrofitService = retrofit.create(service);
//                        return retrofitService;
//                    }
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    private T getRetrofitService() {
                        return getService(service);
                    }

                    private Method getRetrofitMethod(T service, Method method) throws NoSuchMethodException {
                        return service.getClass().getMethod(method.getName(), method.getParameterTypes());
                    }
                });
    }

}
