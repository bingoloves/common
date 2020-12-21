package cn.cqs.baselib.http;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bingo on 2020/12/18.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 网络相关的配置
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/18
 */
public class HttpConfig {
    /**
     * 请填写项目的域名地址
     */
    private String host = "";
    private String token = "token";
    //超时时间
    private long timeout = 10;
    private boolean openLog = true;
    //数据响应拦截器接口
    private ResponseListener mResponseListener;

    private static class HttpConfigSingletonHolder {
        private static final HttpConfig INSTANCE = new HttpConfig();
    }
    public static HttpConfig getHttpConfig(){
        return HttpConfigSingletonHolder.INSTANCE;
    }
    private HttpConfig(){}

    public Retrofit getRetrofit(){
        if (TextUtils.isEmpty(host)) throw new IllegalArgumentException("请先配置HttpConfig中的host域名");
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(getOkHttpClient(timeout,openLog))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    public Retrofit getRetrofit(String baseUrl,OkHttpClient okHttpClient){
        if (TextUtils.isEmpty(baseUrl)) throw new IllegalArgumentException("baseUrl is null");
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public OkHttpClient getOkHttpClient(long timeout,boolean openLog){
        return getOkHttpClient(timeout,true,null);
    }
    public OkHttpClient getOkHttpClient(Interceptor interceptor){
        return getOkHttpClient(timeout,true,interceptor);
    }
    /**
     * 自定义拦截器
     * @param timeout
     * @param openLog
     * @param interceptor
     * @return
     */
    public OkHttpClient getOkHttpClient(long timeout, boolean openLog,Interceptor interceptor){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout,TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new TokenInterceptor());
        if (openLog){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        if (interceptor != null){
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * BaseUrl 必须以'/'结尾
     * @return
     */
    public String getBaseUrl() {
        return host+"/";
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isOpenLog() {
        return openLog;
    }

    public void setOpenLog(boolean openLog) {
        this.openLog = openLog;
    }

    public ResponseListener getResponseListener() {
        return mResponseListener;
    }
    /**
     * 设置拦截器
     * @param responseListener
     */
    public void setResponseHandler(ResponseListener responseListener) {
        this.mResponseListener = responseListener;
    }
}
