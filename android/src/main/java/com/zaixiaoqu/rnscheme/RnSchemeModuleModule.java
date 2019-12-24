package com.zaixiaoqu.rnscheme;

import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RnSchemeModuleModule extends ReactContextBaseJavaModule {


    public static final String OpenSchemeDidReceiveMessage = "OpenSchemeDidReceiveMessage";
    private static ReactApplicationContext reactContext;
    private static WritableMap OpenSchemeDidReceiveMessageParams;


    public RnSchemeModuleModule(ReactApplicationContext reactContext) {
        super(reactContext);
        RnSchemeModuleModule.reactContext = reactContext;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(OpenSchemeDidReceiveMessage, OpenSchemeDidReceiveMessage);
        return constants;
    }

    @Override
    public String getName() {
        return "RnSchemeModule";
    }

    /**
     * 发送事件
     */
    public static void sendOpenSchemeDidReceiveMessage(String scheme, String encodedPath, String queryString) {
        String eventName = OpenSchemeDidReceiveMessage;
        if (null == encodedPath) {
            encodedPath = "";
        }
        if (null == queryString) {
            queryString = "";
        }
        try {
            encodedPath = URLDecoder.decode(encodedPath, "UTF-8" );
            queryString = URLDecoder.decode(queryString, "UTF-8" );
        } catch (Exception e) {
        }

        WritableMap params = Arguments.createMap();
        params.putString("scheme", scheme);
        params.putString("encodedPath", encodedPath);
        params.putString("queryString", queryString);
        if (null == reactContext) {
            OpenSchemeDidReceiveMessageParams = params;
            return;
        } else {
        }
        setEvent(eventName, params);
    }

    /**
     * 给RN js发送事件
     *
     * @param eventName
     * @param params
     */
    private static void setEvent(String eventName, WritableMap params) {
        //此处需要添加hasActiveCatalystInstance，否则可能造成崩溃
        //问题解决参考: https://github.com/walmartreact/react-native-orientation-listener/issues/8
        try {
            if(reactContext.hasActiveCatalystInstance()) {
                try {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit(eventName, params);
                } catch (Exception e) {
                }
            } else {
            }
        } catch (Exception e) {
        }
    }


    /**
     * 唤醒首次打开的等待消息
     *
     * @param promise
     */
    @ReactMethod
    public void awakenWaitOpenMessage(Promise promise) {
        if (null == OpenSchemeDidReceiveMessageParams) {
            return;
        }
        String eventName = OpenSchemeDidReceiveMessage;
        setEvent(eventName, OpenSchemeDidReceiveMessageParams);
        OpenSchemeDidReceiveMessageParams = null;
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }
}
