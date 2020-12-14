package cn.cqs.baselib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cqs.baselib.R;
import cn.cqs.baselib.utils.AnyLayerHelper;
import cn.cqs.baselib.utils.Injector;

/**
 * Created by bingo on 2020/11/25.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/25
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 当前Activity
     */
    protected Activity activity;
    private Unbinder unBinder;
    protected Handler mHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityEnterAnimation();
        mHandler = new Handler(Looper.getMainLooper());
        activity = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unBinder = ButterKnife.bind(this);
        //注入工具
        Injector.inject(this);
        //初始化沉浸式
        initImmersionBar();
    }
    /**
     * 进入动画设置
     */
    protected void setActivityEnterAnimation(){
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 退出动画
     */
    protected void setActivityExitAnimation(){
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    /**
     * 精简toast
     * @param msg
     */
    protected void toast(String msg){
        //Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        AnyLayerHelper.showToast(this,msg);
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        ImmersionBar.with(this).init();
    }

    /**
     * 是否关闭自身Activity
     */
    private boolean isFinishSelf = false;

    public void setFinishSelf(boolean finishSelf) {
        this.isFinishSelf = finishSelf;
    }

    /**
     * 页面跳转
     * @param cls
     */
    protected void navigateTo(Class<?> cls){
        navigateTo(cls,false);
    }
    protected void navigateTo(Class<?> cls,boolean isFinishSelf){
        navigateTo(new Intent(activity,cls),isFinishSelf);
    }
    protected void navigateTo(Intent intent,boolean isFinishSelf){
        startActivity(intent);
        this.isFinishSelf = isFinishSelf;
        if (isFinishSelf)finish();
    }

    @Override
    public void finish() {
        super.finish();
        mHandler.postDelayed(this::setActivityExitAnimation,isFinishSelf ? 300 : 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unBinder != null){
            unBinder.unbind();
        }
    }
}
