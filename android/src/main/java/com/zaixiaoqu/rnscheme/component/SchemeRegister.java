package com.zaixiaoqu.rnscheme.component;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SchemeRegister {

    /**
     * 主程序Activity是否在运行中
     */
    private static boolean isRuningForMainActivity = false;

    /**
     * 初始化参数
     * 唤醒模拟器命令
     * adb shell am start -W -a android.intent.action.VIEW -d "llt://mychat/chat/Eric" com.llt.jobpost
     */
    public static void init() {
        isRuningForMainActivity = true;
    }

    /**
     * 判断主程序Activity是否在运行中
     *
     * @return
     */
    public static boolean isRuningMainActivity() {
        return isRuningForMainActivity;
    }
}
