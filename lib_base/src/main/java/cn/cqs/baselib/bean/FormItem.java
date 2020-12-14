package cn.cqs.baselib.bean;

import android.text.TextWatcher;
import android.view.View;

/**
 * Created by bingo on 2020/12/14.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 表单类的Item实体类
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/14
 */
public class FormItem {
    /**
     * 表单图标
     */
    public int icon;
    /**
     * 表单名称
     */
    public String name;
    /**
     * 表单值
     */
    public String value;
    /**
     * 是否有分割线
     */
    public boolean hasLine;
    /**
     * 是否是输入框类型
     */
    public boolean isInputMode;
    /**
     * 是否是选择类型
     */
    public boolean isSelectMode;
    /**
     * 点击事件接口
     */
    public View.OnClickListener clickListener;
    /**
     * 编辑的接口
     */
    public TextWatcher textWatcher;


    /**
     * @param icon
     * @param name
     * @param value
     * @param hasLine
     * @param isInputMode
     * @param isSelectMode
     * @param clickListener
     * @param textWatcher
     */
    public FormItem(int icon, String name, String value, boolean hasLine, boolean isInputMode, boolean isSelectMode, View.OnClickListener clickListener, TextWatcher textWatcher) {
        this.icon = icon;
        this.name = name;
        this.value = value;
        this.hasLine = hasLine;
        this.isInputMode = isInputMode;
        this.isSelectMode = isSelectMode;
        this.clickListener = clickListener;
        this.textWatcher = textWatcher;
    }

    /**
     * 不包含Icon类型的
     * @param name
     * @param value
     * @param hasLine
     * @param clickListener
     */
    public FormItem(String name, String value, boolean hasLine, View.OnClickListener clickListener) {
        this.name = name;
        this.value = value;
        this.hasLine = hasLine;
        this.clickListener = clickListener;
        this.isSelectMode = true;
        this.isInputMode = false;
    }
    /**
     * 简单模式 个人中心的类型
     * @param icon
     * @param name
     * @param value
     * @param hasLine
     * @param clickListener
     */
    public FormItem(int icon, String name, String value, boolean hasLine, View.OnClickListener clickListener) {
        this.icon = icon;
        this.name = name;
        this.value = value;
        this.hasLine = hasLine;
        this.clickListener = clickListener;
        this.isSelectMode = true;
        this.isInputMode = false;
    }

    /**
     * 输入框模式
     * @param icon
     * @param name
     * @param value
     * @param hasLine
     * @param textWatcher
     */
    public FormItem(int icon, String name, String value, boolean hasLine, TextWatcher textWatcher) {
        this.icon = icon;
        this.name = name;
        this.value = value;
        this.hasLine = hasLine;
        this.textWatcher = textWatcher;
        this.isSelectMode = false;
        this.isInputMode = true;
    }
}
