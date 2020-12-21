package cn.cqs.baselib.http;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String tokenKey = HttpConfig.getHttpConfig().getToken();
        String token = MMKVHelper.decodeString(tokenKey);
        if (!TextUtils.isEmpty(token)){
            Request updateRequest = request.newBuilder().header(tokenKey, token).build();
            return chain.proceed(updateRequest);
        }
        return chain.proceed(request);
    }
}
