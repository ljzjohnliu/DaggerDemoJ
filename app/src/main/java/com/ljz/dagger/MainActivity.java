package com.ljz.dagger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ljz.dagger.basicuse.Bird;
import com.ljz.dagger.basicuse.Cat;
import com.ljz.dagger.basicuse.DaggerMainComponent;
import com.ljz.dagger.seconduse.Flower;
import com.ljz.dagger.seconduse.MainModule;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /**
     * 在 MainActivity 中创建了一个 cat变量，并加上了 @Inject注解，来告诉 Dagger2 你要为cat赋值，即依赖注入。
     * 所以 MainActivity 就是依赖的需求方。
     */
    @Inject
    Cat cat;

    @Inject
    Bird bird;

    @Inject
    Flower flower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * DaggerMainComponent就是真正的依赖注入组件，它是MainComponent接口编译生成的。
         * 至此，一个最简单的dagger使用实例完成
         */
        DaggerMainComponent.builder()
                .mainModule(new MainModule())//这一行不写也是OK的，看下 DaggerMainComponent 实现就清楚了
                .build()
                .inject(this);
        Log.d(TAG, "onCreate, cat: " + cat.toString());
        Log.d(TAG, "onCreate, bird: " + bird.toString());
        Log.e(TAG, "onCreate, flower: " + flower.toString());
    }
}