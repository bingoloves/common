package cn.cqs.baselib.utils;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import cn.cqs.baselib.R;
import cn.cqs.baselib.adapter.recyclerview.CommonAdapter;
import cn.cqs.baselib.adapter.recyclerview.base.ViewHolder;
import cn.cqs.baselib.bean.PopupMenu;
import per.goweii.anylayer.Align;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;

/**
 * Created by bingo on 2020/9/27.
 */
public class PopupUtils {

    /**
     * popup
     * @param targetView  当前相对位置View
     * @param popupMenu  数据源包装类
     *@param offsetYdp   Y方向的偏移量
     */
    public static DialogLayer singleSelect(final View targetView, PopupMenu popupMenu, int offsetYdp, int layoutItem, OnBindViewLisener bindViewLisener, AnyLayerHelper.OnPopupClickListener popupClickListener){
        Context context = targetView.getContext();
        List<String> menus = popupMenu.getMenusList();
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_common_list,null);
        DialogLayer dialogLayer = AnyLayer.popup(targetView)
                .align(Align.Direction.VERTICAL, Align.Horizontal.ALIGN_RIGHT, Align.Vertical.BELOW, true)
                .offsetYdp(offsetYdp)
                .backgroundDimDefault()
                .outsideTouchedToDismiss(true)
                .outsideInterceptTouchEvent(true)
                .contentView(contentView)
                .contentAnimator(new DialogLayer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(@NonNull View content) {
                        return AnimatorHelper.createTopInAnim(content);
                    }

                    @Override
                    public Animator createOutAnimator(@NonNull View content) {
                        return AnimatorHelper.createTopOutAnim(content);
                    }
                });
        RecyclerView popupRv = contentView.findViewById(R.id.commonRv);
        popupRv.setLayoutManager(new LinearLayoutManager(context));
        ((SimpleItemAnimator)popupRv.getItemAnimator()).setSupportsChangeAnimations(false);
        CommonAdapter adapter = new CommonAdapter<String>(context,layoutItem,menus) {
            @Override
            protected void convert(ViewHolder holder, String value, int position) {
                if (bindViewLisener != null){
                    bindViewLisener.convert(holder,value,position);
                }
                holder.itemView.setOnClickListener(v -> {
                    if (popupMenu.getIndex() != position){
                        if (popupMenu.getIndex() != -1)notifyItemChanged(popupMenu.getIndex());
                        notifyItemChanged(position);
                    }
                    popupMenu.setIndex(position);
                    targetView.postDelayed(() -> {
                        dialogLayer.dismiss();
                        if (popupClickListener != null)popupClickListener.onClick(position,value);
                    },300);
                });
            }
        };
        popupRv.setAdapter(adapter);
       return dialogLayer;
    }
    /**
     * 数据绑定接口
     */
    public interface OnBindViewLisener{
        void convert(ViewHolder holder, String value, int position);
    }
}

