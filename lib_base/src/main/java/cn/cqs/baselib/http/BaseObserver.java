package cn.cqs.baselib.http;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import cn.cqs.baselib.utils.AnyLayerUtils;
import cn.cqs.baselib.utils.log.LogUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import per.goweii.anylayer.DialogLayer;
import retrofit2.HttpException;

/**
 * 优化处理通用观察者，将数据统一处理逻辑已接口的形式供外部处理
 * @param <T>:原始对象
 */
public abstract class BaseObserver<T> implements Observer<T> {

    private Disposable d;
    private DialogLayer loading;
    //持续时间最小1500ms保证体验效果
    private static final int MIN_TIME = 1500;
    private long startTime = 0;
    //是否显示加载框
    private boolean showLoading = false;
    //是否等待加载
    private boolean isWaitLoading = false;
    /**
     * 主线程Handler,主要负责加载框的UI显示
     */
    private Handler mainHandler;

    /**
     * 数据响应接口
     * <p>
     *     doneAfter:只负责响应拦截类型
     * </p>
     */
    private ResponseListener mResponseListener;

    public abstract void onSuccess(T data);

    public abstract void onFailure(String error);

    public BaseObserver() { }

    public BaseObserver(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public BaseObserver(boolean showLoading,boolean isWaitLoading) {
        this.showLoading = showLoading;
        this.isWaitLoading = isWaitLoading;
    }

    /**
     * 订阅在执行请求的线程中，执行的结果回调却是在主线程中
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (loading == null){
            loading = AnyLayerUtils.loading();
        }
        if (isWaitLoading){
            loading.cancelableOnTouchOutside(false);
            loading.cancelableOnClickKeyBack(false);
        }
        mainHandler = getMainHandler();
        if (showLoading){
            mainHandler.post(() -> loading.show());
        }
        startTime = System.currentTimeMillis();
        mResponseListener = HttpConfig.getHttpConfig().getResponseListener();
    }

    @Override
    public void onNext(final T data) {
        if (mResponseListener != null) {
            ResponseResult responseResult = mResponseListener.handle(data);
            if (responseResult == null){
                handleDialogEvent(() -> {
                    hideDialog();
                    onSuccess(data);
                });
            } else {
                int type = responseResult.type;
                switch (type){
                    case ResponseResult.TYPE_ERROR:
                    case ResponseResult.TYPE_PASS:
                        handleDialogEvent(() -> {
                            hideDialog();
                            if (type == ResponseResult.TYPE_ERROR){
                                onFailure(responseResult.msg);
                            } else {
                                onSuccess(data);
                            }
                        });
                        break;
                    case ResponseResult.TYPE_INTERCEPT://拦截状态,外部处理，这里只负责关闭当前请求,将处理方式用接口抛出
                        mainHandler.postDelayed(() -> mResponseListener.doneAfter(responseResult.code),500);
                        break;
                    default:
                        break;
                }
            }
        } else {
            handleDialogEvent(() -> {
                hideDialog();
                onSuccess(data);
            });
        }
//        if (data instanceof BaseResponse){
//            BaseResponse response =  (BaseResponse) data;
//            Object object = response.getData();
//            if (object != null){
//                int code = response.getCode();
//                String msg = response.getMsg();
//                handleResponse(code,data,msg);
//            } else {
//              onFailure("数据异常");
//            }
//        } else if (data instanceof BaseListResponse){
//            BaseListResponse response =  (BaseListResponse) data;
//            List responseData = response.getData();
//            if (responseData != null){
//                int code = response.getCode();
//                String msg = response.getMsg();
//                handleResponse(code,data,msg);
//            } else {
//                onFailure("数据异常");
//            }
//        }
    }

    /**
     * 统一数据处理
     */
//    private void handleResponse(int code, T data, String msg){
//        if (code == 1){
//            hideDialog();
//            onSuccess(data);
//        } else if (code == 0){
//            hideDialog();
//            onFailure(msg);
//        } else {
////            Activity topActivity = ActivityStackManager.getStackManager().currentActivity();
////            if (topActivity != null && !"cn.cncqs.zc.activity.LoginActivity".equals(topActivity.getClass().getCanonicalName())){
////                Intent intent = new Intent(topActivity,LoginActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                topActivity.startActivity(intent);
////            }
//        }
//    }

    /**
     * 进入到onError 不会进入到 onComplete
     * @param e
     */
    @Override
    public void onError(final Throwable e) {
        onComplete();
        onFailure(exceptionHandler(e));
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        handleDialogEvent(this::hideDialog);
    }

    /**
     * 处理dialog 显示效果
     */
    private void handleDialogEvent(Runnable runnable){
         long endTime = System.currentTimeMillis();
         long timeDiff = endTime - startTime;
         if (timeDiff > MIN_TIME){
             mainHandler.post(runnable);
         } else {
             mainHandler.postDelayed(runnable,MIN_TIME - timeDiff);
         }
    }

    /**
     * 隐藏dialog
     */
    private void hideDialog(){
        if (loading != null && loading.isShow()){
            loading.dismiss();
        }
    }

    /**
     * 异常处理
     * @param e
     * @return 返回错误信息
     */
    private String exceptionHandler(Throwable e){
        String errorMsg = "未知错误";
        if (e instanceof UnknownHostException) {
            errorMsg = "网络不可用";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "请求网络超时";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException) {
            errorMsg =  "数据解析错误";
        }
        return errorMsg;
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg =  "服务器处理请求出错";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg =  "服务器无法处理请求";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg =  "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
    /**
     * 判断是否是主线程
     * @return
     */
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 获取主线程handler
     * @return
     */
    public Handler getMainHandler(){
        return new Handler(Looper.getMainLooper());
    }
}
