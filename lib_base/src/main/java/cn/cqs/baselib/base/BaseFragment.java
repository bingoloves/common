package cn.cqs.baselib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cqs.baselib.skin.listener.IDynamicNewView;
import cn.cqs.baselib.skin.listener.ISkinUpdate;
import cn.cqs.baselib.utils.AnyLayerHelper;
import cn.cqs.baselib.utils.Injector;

/**
 * Created by bingo on 2020/11/24.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/24
 */
public abstract class BaseFragment extends Fragment implements ISkinUpdate, IDynamicNewView {
    private Unbinder unbinder;
    protected String mTag = this.getClass().getSimpleName();
    protected View mContentView;
    protected abstract int getLayoutId();
    protected abstract void initView();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mContentView == null){
            mContentView = inflater.inflate(getLayoutId(), container, false);
        }
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder= ButterKnife.bind(this, view);
        Injector.inject(this);/**注入工具*/
        initView();
    }

    /**
     * 简化toast
     * @param msg
     */
    protected void toast(CharSequence msg){
        AnyLayerHelper.showToast(getContext(),msg);
    }
    /**
     * 创建fragment的静态方法，方便传递参数
     * @param args 传递的参数
     * @return
     */
    public static <T extends Fragment>T newInstance(Class clazz, Bundle args) {
        T mFragment=null;
        try {
            mFragment= (T) clazz.newInstance();
            mFragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFragment;
    }

    /**
     * 提供简化的跳转页面方法
     * @param cls
     */
    protected void navigateTo(Class<?> cls){
        navigateTo(cls,false);
    }
    protected void navigateTo(Class<?> cls,boolean isFinishSelf){
        navigateTo(new Intent(getContext(),cls),isFinishSelf);
    }
    protected void navigateTo(Intent intent,boolean isFinishSelf){
        startActivity(intent);
        if (getActivity() != null){
            if (isFinishSelf)getActivity().finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null){
            unbinder.unbind();
        }
    }
}
