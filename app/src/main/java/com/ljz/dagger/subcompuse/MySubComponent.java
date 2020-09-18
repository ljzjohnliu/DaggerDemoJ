package com.ljz.dagger.subcompuse;

import com.ljz.dagger.activity.SubActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {SubModule.class})
public interface MySubComponent {
    void inject(SubActivity activity);
}
