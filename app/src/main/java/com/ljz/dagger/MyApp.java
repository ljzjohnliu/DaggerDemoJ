package com.ljz.dagger;

import android.app.Application;

import com.ljz.dagger.common.CommonComponent;
import com.ljz.dagger.common.CommonModule;
import com.ljz.dagger.common.DaggerCommonComponent;

public class MyApp extends Application {

    private CommonComponent commonComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        commonComponent = DaggerCommonComponent.builder().commonModule(new CommonModule()).build();
    }

    public CommonComponent getCommonComponent() {
        return commonComponent;
    }
}
