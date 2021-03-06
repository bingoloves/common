package cn.cqs.baselib.plugin.interfaces;

import android.content.Context;
import android.content.Intent;

/**
 * Created by bingo on 2020/11/23.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: BroadcastReceiver插件规则定义
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/23
 */
public interface BroadcastReceiverInterface {
    void onReceive(Context context, Intent intent);
}
