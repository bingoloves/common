package cn.cqs.android;

import cn.cqs.baselib.base.StandardActivity;
import cn.cqs.baselib.utils.AnyLayerUtils;

public class MainActivity extends StandardActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setTitle("标准Activity");
        findViewById(R.id.btn_test).setOnClickListener(v -> {
            AnyLayerUtils.showToast(this,"Hello World");
        });
    }
}
