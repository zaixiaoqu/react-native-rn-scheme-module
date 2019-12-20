package com.zaixiaoqu.rnscheme.component;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class SchemeActivity extends Activity {

    /**
     * 得到主Activity
     *
     * @return
     */
    protected abstract Class getMainActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // React Native 自定义唤醒app协议 初始化
        this.registerSchemeAnalysis();
        if (!SchemeRegister.isRuningMainActivity()) {
            Intent intent = this.getIntent();
            intent.setClass(this.getApplicationContext(), this.getMainActivity());
            startActivity(intent);
        }
        finish();
    }

    /**
     * 解析Scheme参数
     */
    private void registerSchemeAnalysis() {

    }
}
