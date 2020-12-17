package cn.cqs.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import java.util.ArrayList;
import cn.cqs.baselib.R;
import cn.cqs.baselib.bean.TabEntity;

/**
 * Created by bingo on 2020/11/11.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 自定义Toolbar
 * @UpdateUser: bingo
 * @UpdateDate: 2020/11/11
 */
public class CustomToolbar extends FrameLayout {
    private Toolbar mToolbar;
    private TextView mTitleTv;
    private SegmentTabLayout mSegmentTabLayout;
    private CommonTabLayout mCommonTabLayout;
    private ImageView mBackIv;
    private ImageView mCloseIv;
    private ImageView mMoreIv;
    private TextView mMoreTextTv;
    //标题相关的
    private Drawable mLeftIcon;
    private String mTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;
    /**
     * 提供外部接口
     */
    private View.OnClickListener onBackClickListener;
    private View.OnClickListener onMenuClickListener;

    public CustomToolbar(Context context) {
        super(context);
        initView(context,null);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.toolbar, this);
        if (attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar);
            if (typedArray != null) {
                mLeftIcon = typedArray.getDrawable(R.styleable.CustomToolbar_left_icon);
                mTitleText = typedArray.getString(R.styleable.CustomToolbar_title);
                mTitleTextColor = typedArray.getColor(R.styleable.CustomToolbar_title_text_color, Color.WHITE);
                mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomToolbar_title_text_size, 16);
                typedArray.recycle();
            }
        }
        mToolbar = findViewById(R.id.toolbar);
        mTitleTv = findViewById(R.id.toolbar_title);
        mSegmentTabLayout = findViewById(R.id.toolbar_segment_tabLayout);
        mCommonTabLayout = findViewById(R.id.toolbar_common_tabLayout);
        mBackIv = findViewById(R.id.toolbar_back);
        mCloseIv = findViewById(R.id.toolbar_close);
        mMoreIv = findViewById(R.id.toolbar_more);
        mMoreTextTv = findViewById(R.id.toolbar_more_text);
        //返回键处理
        mBackIv.setOnClickListener(backClickListener);
        mCloseIv.setOnClickListener(backClickListener);
        mMoreIv.setOnClickListener(menuClickListener);
        mMoreTextTv.setOnClickListener(menuClickListener);
        //初始化相关数据
        if (mLeftIcon != null){
            mBackIv.setImageDrawable(mLeftIcon);
        }
        if (mTitleText != null){
            mTitleTv.setText(mTitleText);
        }
        mTitleTv.setTextColor(mTitleTextColor);
        mTitleTv.setTextSize(mTitleTextSize);
        mCommonTabLayout.setTextsize(mTitleTextSize);
        mSegmentTabLayout.setTextsize(mTitleTextSize);
    }


    /**
     * 返回按钮监听
     */
    private View.OnClickListener backClickListener = v -> {
        int id = v.getId();
        if (id == R.id.toolbar_back || id == R.id.toolbar_close){
            if (onBackClickListener != null){
                onBackClickListener.onClick(v);
            }
        }
    };
    /**
     * 右侧按钮的事件
     */
    private View.OnClickListener menuClickListener = v -> {
        int id = v.getId();
        if (id == R.id.toolbar_more || id == R.id.toolbar_more_text){
            if (onMenuClickListener != null){
                onMenuClickListener.onClick(v);
            }
        }
//        switch (v.getId()){
//            case R.id.toolbar_more:
//            case R.id.toolbar_more_text:
//                if (onMenuClickListener != null){
//                    onMenuClickListener.onClick(v);
//                }
//                break;
//            default:
//                break;
//        }
    };

    /**
     * 设置分段头部
     */
    public void setSegmentTabTitle(String[] titles){
        mSegmentTabLayout.setTabData(titles);
        mSegmentTabLayout.setVisibility(View.VISIBLE);
        mCommonTabLayout.setVisibility(View.GONE);
        mTitleTv.setVisibility(View.GONE);
    }
    /**
     * 设置通用Tab头部
     */
    public void setCommonTabTitle(String[] titles){
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], 0,0));
        }
        mCommonTabLayout.setTabData(mTabEntities);
        mCommonTabLayout.setVisibility(View.VISIBLE);
        mSegmentTabLayout.setVisibility(View.GONE);
        mTitleTv.setVisibility(View.GONE);
    }

    /**
     * 设置标准文本头部
     * @param title
     */
    public void setTitle(CharSequence title) {
        mTitleTv.setText(title);
    }
    public void hideBackIcon(){
        mBackIv.setVisibility(GONE);
    }
    public void setBackIcon(@DrawableRes int resId){
        mBackIv.setImageResource(resId);
    }
    public void setMenuIcon(@DrawableRes int resId){
        mMoreIv.setImageResource(resId);
    }
    public void setMenuText(String value){
        mMoreTextTv.setText(value);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public SegmentTabLayout getSegmentTabLayout() {
        return mSegmentTabLayout;
    }

    public CommonTabLayout getCommonTabLayout() {
        return mCommonTabLayout;
    }

    public TextView getTitleTextView() {
        return mTitleTv;
    }

    public ImageView getBackImageView() {
        return mBackIv;
    }

    public ImageView getCloseImageView() {
        return mCloseIv;
    }

    public ImageView getMenuImageView() {
        return mMoreIv;
    }

    public TextView getMenuTextView() {
        return mMoreTextTv;
    }

    /**
     * 外部接口
     * @param onBackClickListener
     */
    public void setOnBackClickListener(View.OnClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public void setOnMenuClickListener(View.OnClickListener menuClickListener) {
        this.onMenuClickListener = menuClickListener;
    }
}
