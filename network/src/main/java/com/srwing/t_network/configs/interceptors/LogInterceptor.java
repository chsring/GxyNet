package com.srwing.t_network.configs.interceptors;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.MapUtils;
import com.srwing.t_network.configs.configs.Configurator;
import com.srwing.t_network.configs.utils.GxyLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author srwing
 * @fileName ByInterceptor
 * @time 2022/6/17 2:33 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc 基础日志拦截器
 */

public class LogInterceptor extends BaseInterceptor {

    private final static String TAG = "NET";
    private StringBuffer stringBuffer = new StringBuffer();

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //***********************请求前*****************************
        Request request = chain.request();
        stringBuffer.setLength(0);
        stringBuffer.append("REQUEST --------->\n");
        LinkedHashMap<String, String> urlParameters = getUrlParameters(chain);
        if (!MapUtils.isEmpty(urlParameters)) {
            stringBuffer.append("UrlParameter:").append(urlParameters).append("\n");
        }
        LinkedHashMap<String, String> bodyParameters = getBodyParameters(chain);
        if (!MapUtils.isEmpty(bodyParameters)) {
            stringBuffer.append("BodyParameter:").append(bodyParameters).append("\n");
        }
        GxyLogger.i(TAG, stringBuffer.toString());
        //***********************请求中*****************************
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);
        long endTime = System.currentTimeMillis();
        //*********************** 请求后*****************************
        ResponseBody body = response.body();
        if (!Configurator.isDebugMode()) {
            return response;
        }
        if (body == null)
            return response;

        if (!bodyEncoded(response.headers())) {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();

            Charset charset = UTF8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }
            if (!isPlaintext(buffer)) {
                return response;
            }
            String result = buffer.clone().readString(charset);
            //得到所需的string，开始判断是否异常
            //***********************do something*****************************
            String timeResuming = cacuTimeResuming(startTime, endTime);
            stringBuffer.setLength(0);
            stringBuffer.append("RESPONSE <---------\n");
            stringBuffer.append(response).append("\n")
                    .append("Result:").append(result).append("\n")
                    .append("TimeConsuming:").append(timeResuming);
            GxyLogger.i(TAG, stringBuffer.toString());
//            GxyLogger.i(TAG,"RESPONSE--------->" + "\n" +
////                    "headers:{\n" +headers.toString() +"}\n"/*+request.toString() + "\n"*/ +
//                    "UrlParameter:" + urlParameters + "\n" +
//                    "BodyParameter:" + bodyParameters + "\n" +
//                    response + "\n" +
//                    "Result:" + result + "\n" +
//                    "TimeConsuming:" + timeResuming);
//            GxyLogger.json(TAG, result);
        }
        return response;
    }


    private String cacuTimeResuming(long startTime, long endTime) {
        long timeConsuming = endTime - startTime;
        return timeConsuming + "ms";
    }
}
