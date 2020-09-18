package com.ljz.dagger.subcompuse;

import com.ljz.dagger.qualifieruse.Flower;

import dagger.Module;
import dagger.Provides;

@Module
public class SubModule {
    @Provides
    public Flower provideFlower() {
        return new Flower("腊梅", "红色");
    }
}
