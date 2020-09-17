package com.ljz.dagger.thirduse;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailModule {

//    @Singleton
    @DetailActivityScope
    @Provides
    public Book provideBook() {
        return new Book("Kotlin 指南", 66.8f);
    }
}
