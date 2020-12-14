package cn.cqs.baselib.http;

/**
 * Created by bingo on 2020/10/16.
 */

public interface Api {
    /**
     * 登录接口
     */
    String LOGIN = "/api/index/login";
    /**
     * 上传
     */
    String UPLOAD = "/api/uploads/trace";
    /**
     * 登出
     */
    String LOGOUT = "/api/index/logout";
}
