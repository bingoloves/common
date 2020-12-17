package cn.cqs.baselib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cqs.baselib.R;
import cn.cqs.baselib.skin.entity.DynamicAttr;
import cn.cqs.baselib.skin.listener.IDynamicNewView;
import cn.cqs.baselib.skin.listener.ISkinUpdate;
import cn.cqs.baselib.skin.loader.SkinInflaterFactory;
import cn.cqs.baselib.skin.loader.SkinManager;
import cn.cqs.baselib.utils.AnyLayerUtils;
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
public class BaseActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {
    /**
     * 当前Activity
     */
    protected Activity activity;
    protected Unbinder unBinder;
    protected Handler mHandler;
    /**
     * Whether response to skin changing after create
     */
    private boolean isResponseOnSkinChanging = true;

    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory();
        getLayoutInflater().setFactory(mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
        setActivityEnterAnimation();
        mHandler = new Handler(Looper.getMainLooper());
        activity = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initButterKnife();
        //注入工具
        Injector.inject(this);
        //初始化沉浸式
        initImmersionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
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
        AnyLayerUtils.showToast(this,msg);
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
        if (isFinishSelf){
            mHandler.postDelayed(this::setActivityExitAnimation,300);
        } else {
            setActivityExitAnimation();
        }
    }
    protected void initButterKnife(){
        unBinder = ButterKnife.bind(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
        if (unBinder != null){
            unBinder.unbind();
        }
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    @Override
    public void onThemeUpdate() {
        if(!isResponseOnSkinChanging){
            return;
        }
        mSkinInflaterFactory.applySkin();
    }
    /**
     * dynamic add a skin view
     *
     * @param view
     * @param attrName
     * @param attrValueResId
     */
    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId){
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs){
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    final protected void enableResponseOnSkinChanging(boolean enable){
        isResponseOnSkinChanging = enable;
    }
}
