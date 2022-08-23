package com.example.biz.user.app;

import android.app.Application;

import com.example.biz.user.ToastStyle;
import com.hjq.toast.ToastUtils;

/**
 * @ClassName: AppApplication
 * @Description TODO
 * @Author zhuangyuan.ji
 * @Date 2022/8/23
 **/
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initSdk(this);
    }

    /**
     * 初始化一些第三方框架
     */
    private void initSdk(AppApplication application) {
        // 初始化吐司
        ToastUtils.init(application, new ToastStyle());
    }
}
