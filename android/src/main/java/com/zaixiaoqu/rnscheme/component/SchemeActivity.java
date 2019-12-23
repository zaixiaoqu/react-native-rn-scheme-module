package com.zaixiaoqu.rnscheme.component;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zaixiaoqu.rnscheme.RnSchemeModuleModule;

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
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri == null) {
            return;
        }
        String scheme = uri.getScheme();
        if (null == uri || scheme.isEmpty() || scheme.equals("")) {
            return;
        }
        String encodedPath = uri.getHost() + uri.getEncodedPath();
        String queryString = uri.getEncodedQuery();
        RnSchemeModuleModule.sendOpenSchemeDidReceiveMessage(scheme, encodedPath, queryString);
    }
}
