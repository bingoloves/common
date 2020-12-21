package cn.cqs.baselib.http;

/**
 * retrofit 工具封装
 */
public class RetrofitUtil {
    /**
     * 使用前，请先配置HttpConfig中的Host域名
     * @return
     */
    public static ApiService getApiService() {
        return HttpConfig.getHttpConfig().getRetrofit().create(ApiService.class);
    }
}
