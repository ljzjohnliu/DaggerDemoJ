package com.ljz.dagger.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.dagger.R;
import com.ljz.dagger.qualifieruse.Flower;
import com.ljz.dagger.singletonuse.Book;
import com.ljz.dagger.singletonuse.DaggerDetailComponent;
import com.ljz.dagger.singletonuse.DetailComponent;
import com.ljz.dagger.singletonuse.DetailModule;
import com.ljz.dagger.subcompuse.SubModule;

import javax.inject.Inject;

public class SubActivity extends AppCompatActivity {
    private static final String TAG = "SubActivity";

    @Inject
    Book book;

    @Inject
    Flower flower;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SubActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        // 创建父组件对象
        DetailComponent detailComponent = DaggerDetailComponent.builder()
                .detailModule(new DetailModule())
                .build();
        // 得到子组件，并完成依赖注入
        detailComponent.getSubComponent(new SubModule()).inject(this);

        Log.d(TAG, "onCreate, book : " + book.toString());
        Log.d(TAG, "onCreate, flower : " + flower.toString());
    }
}