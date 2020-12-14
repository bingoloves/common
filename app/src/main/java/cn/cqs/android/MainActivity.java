package cn.cqs.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.cqs.baselib.utils.AnyLayerHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_test).setOnClickListener(v -> {
            AnyLayerHelper.showToast(this,"Hello World");
        });
    }
}
