package com.ljz.dagger;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.dagger.thirduse.Book;
import com.ljz.dagger.thirduse.DaggerDetailComponent;
import com.ljz.dagger.thirduse.DetailModule;

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
                .build()
                .inject(this);

        Log.d(TAG, "onCreate, book1 : " + book1.toString());
        Log.d(TAG, "onCreate, book2 : " + book2.toString());
    }
}