package com.srwing.t_network.configs.interceptors;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author srwing
 * @fileName BaseInterceptor
 * @time 2022/6/17 2:37 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public abstract class BaseInterceptor implements Interceptor {


    protected LinkedHashMap<String, String> getUrlParameters(Chain chain) {
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        final HttpUrl url = chain.request().url();
        params.put("url:",chain.request().url().toString());
        int size = url.querySize();
        for (int i = 0; i < size; i++) {
            params.put(url.queryParameterName(i), url.queryParameterValue(i));
        }
        return params;
    }

    protected String getUrlParameters(Chain chain, String key) {
        final Request request = chain.request();
        return request.url().queryParameter(key);
    }

    protected LinkedHashMap<String, String> getBodyParameters(Chain chain) {
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        try {
            final FormBody formBody = (FormBody) chain.request().body();
            int size = 0;
            if (formBody != null) {
                size = formBody.size();
            }
            for (int i = 0; i < size; i++) {
                params.put(formBody.name(i), formBody.value(i));
            }
        }catch (ClassCastException e){
            RequestBody requestBody = chain.request().body();
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return params;
            }
            //编码设为UTF-8
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            //拿到request
            String requestString = buffer.readString(charset);
            params.put("requestBody:",requestString);
        }
        return params;
    }

    protected String getBodyParameters(Chain chain, String key) {
        return getBodyParameters(chain).get(key);
    }

    protected static final Charset UTF8 = StandardCharsets.UTF_8;

    protected boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    protected boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    protected String getResult(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null)
            return "";
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
                    return "";
                }
            }
            if (!isPlaintext(buffer)) {
                return "";
            }
            return buffer.clone().readString(charset);
        }
        return "";
    }
}
