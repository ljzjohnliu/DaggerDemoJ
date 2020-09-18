package com.ljz.dagger.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ljz.dagger.MyApp;
import com.ljz.dagger.R;
import com.ljz.dagger.basicuse.Bird;
import com.ljz.dagger.basicuse.Cat;
import com.ljz.dagger.basicuse.DaggerMainComponent;
import com.ljz.dagger.qualifieruse.Flower;
import com.ljz.dagger.qualifieruse.MainModule;
import com.ljz.dagger.qualifieruse.QualifierBlue;
import com.ljz.dagger.singletonuse.Book;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Context mContext;

    /**
     * 在 MainActivity 中创建了一个 cat变量，并加上了 @Inject注解，来告诉 Dagger2 你要为cat赋值，即依赖注入。
     * 所以 MainActivity 就是依赖的需求方。
     */
    @Inject
    Cat cat;

    @Inject
    Bird bird;

    @Named("red")
    @Inject
    Flower flower1;

    @Named("white")
    @Inject
    Flower flower2;

    @QualifierBlue
    @Inject
    Flower flower3;

    @Inject
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        findViewById(R.id.to_detail).setOnClickListener(this);

        /**
         * DaggerMainComponent就是真正的依赖注入组件，它是MainComponent接口编译生成的。
         * 至此，一个最简单的dagger使用实例完成
         */
        DaggerMainComponent.builder()
                .mainModule(new MainModule())//这一行不写也是OK的，看下 DaggerMainComponent 实现就清楚了
                .commonComponent(((MyApp)getApplication()).getCommonComponent())
                .build()
                .inject(this);
        Log.d(TAG, "onCreate, cat: " + cat.toString());
        Log.d(TAG, "onCreate, bird: " + bird.toString());
        Log.e(TAG, "onCreate, flower1: " + flower1.toString());
        Log.e(TAG, "onCreate, flower2: " + flower2.toString());
        Log.e(TAG, "onCreate, flower3: " + flower3.toString());
        Log.e(TAG, "onCreate, book: " + book.toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.to_detail:
                intent.setComponent(new ComponentName("com.ljz.dagger", "com.ljz.dagger.activity.DetailActivity"));
                break;
            default:
                Toast.makeText(mContext, "没有有效的跳转页面", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
    }
}