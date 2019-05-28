package com.ben.common.net.hikvision;

import android.util.Base64;

import com.ben.library.log.L;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HikHeaderInterceptor implements Interceptor {

    Map<String, String> headers = new HashMap<>();

    public static final String HOST = "10.19.141.36:443";
    public static final String APP_KEY = "25031676";
    public static final String APP_SECRET = "r7ZfY41RORIrHjNvlEQD";

    @Override
    public Response intercept(Chain chain) throws IOException {



        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        String url = request.url().toString().replace("https://10.19.141.36","");

        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("x-ca-timestamp", String.valueOf((new Date()).getTime()));
        headers.put("x-ca-nonce", UUID.randomUUID().toString());
        headers.put("x-ca-key", APP_KEY);
        headers.put("x-ca-signature", SignUtil.sign(APP_SECRET, "POST", url, headers,
                null, null, new ArrayList<>()).replace("\n",""));

        L.object(headers);
        // process header params inject
        Headers.Builder headerBuilder = request.headers().newBuilder();
        // 以 Entry 添加消息头
        if (headers.size() > 0) {
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.headers(headerBuilder.build());
        }

        request = requestBuilder.build();
        return chain.proceed(request);

    }

}
