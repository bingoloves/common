package cn.cqs.baselib.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.cqs.baselib.R;
import cn.cqs.baselib.bean.TabEntity;
import cn.cqs.baselib.widget.CustomToolbar;

/**
 * Created by bingo on 2020/12/16.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 标准Activity 自带Toolbar/分段头部/自处理返回键
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/16
 */
public abstract class StandardActivity extends BaseActivity {

    protected CustomToolbar customToolbar;
    /**
     * 中间容器
     */
    private FrameLayout mContainer;
    /**
     * 提供外部接口
     */
    private View.OnClickListener onBackClickListener;
    private View.OnClickListener onMenuClickListener;
    /**
     * 中间内容的布局
     * @return
     */
    public abstract int getContentViewId();
    public abstract void initView();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        initRootView();
        initView();
    }
    private void initRootView() {
        //当前的容器
        mContainer = findViewById(R.id.content_container);
        if (getContentViewId() != 0){
            getLayoutInflater().inflate(getContentViewId(), mContainer);
            unBinder = ButterKnife.bind(this);
        }
        customToolbar = findViewById(R.id.custom_toolbar);
        customToolbar.setOnBackClickListener(backClickListener);
        customToolbar.setOnMenuClickListener(menuClickListener);
    }

    /**
     * ButterKnife初始化应该在我们的布局getContentViewId()传递进来后再绑定
     */
    @Override
    protected void initButterKnife() {

    }

    /**
     * 返回按钮监听
     */
    private View.OnClickListener backClickListener = v -> {
        if (onBackClickListener != null){
            onBackClickListener.onClick(v);
        } else {
            finish();
        }
    };
    /**
     * 右侧按钮的事件
     */
    private View.OnClickListener menuClickListener = v -> {
        if (onMenuClickListener != null){
            onMenuClickListener.onClick(v);
        }
    };

    /**
     * 设置分段头部
     */
    protected void setSegmentTabTitle(String[] titles){
        customToolbar.setSegmentTabTitle(titles);
    }
    /**
     * 设置通用Tab头部
     */
    protected void setCommonTabTitle(String[] titles){
        customToolbar.setCommonTabTitle(titles);
    }

    /**
     * 设置标准文本头部
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        customToolbar.setTitle(title);
    }

    public CustomToolbar getCustomToolbar() {
        return customToolbar;
    }

    /**
     * 外部接口
     * @param onBackClickListener
     */
    public void setOnBackClickListener(View.OnClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public void setMenuClickListener(View.OnClickListener menuClickListener) {
        this.onMenuClickListener = menuClickListener;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBar(R.id.toolbar).init();
    }
}
