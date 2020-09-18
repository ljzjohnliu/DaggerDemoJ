package com.ljz.dagger.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.dagger.MyApp;
import com.ljz.dagger.R;
import com.ljz.dagger.singletonuse.Book;
import com.ljz.dagger.singletonuse.DaggerDetailComponent;
import com.ljz.dagger.singletonuse.DetailModule;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    @Inject
    Book book1;

    @Inject
    Book book2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DaggerDetailComponent.builder()
                .detailModule(new DetailModule())
                /**
                 * commonComponent()方法需要一个 CommonComponent 对象，同理在 ShareActivity 中也需要一个 CommonComponent 对象，这里我们要保证两个 CommonComponent 对象是同一个
                 * */
                .commonComponent(((MyApp)getApplication()).getCommonComponent())
                .build()
                .inject(this);

        Log.d(TAG, "onCreate, book1 : " + book1.toString());
        Log.d(TAG, "onCreate, book2 : " + book2.toString());
    }
}