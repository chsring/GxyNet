package com.srwing.newtwork.interceptors;

import androidx.annotation.NonNull;

import com.srwing.newtwork.configs.Configurator;
import com.srwing.newtwork.utils.ByLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedHashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/**
 * Description:
 * Created by srwing
 * Date: 23/11/2018
 * Email: surao@foryou56.com
 */
public class ByInterceptor extends BaseInterceptor {

    private final static String TAG = ByInterceptor.class.getSimpleName();

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {

        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);
        long endTime = System.currentTimeMillis();
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
            LinkedHashMap<String, String> urlParameters = getUrlParameters(chain);
            LinkedHashMap<String, String> bodyParameters = getBodyParameters(chain);

            String timeResuming = cacuTimeResuming(startTime, endTime);

            ByLogger.i(TAG, request.toString() + "\n" +
                    "UrlParameter:" + urlParameters + "\n" +
                    "BodyParameter:" + bodyParameters + "\n" +
                    response + "\n" +
                    "Result:" + result + "\n" +
                    "TimeConsuming:" + timeResuming);
        }
        return response;
    }


    private String cacuTimeResuming(long startTime, long endTime) {
        long timeConsuming = endTime - startTime;
        return timeConsuming + "ms";
    }


}
