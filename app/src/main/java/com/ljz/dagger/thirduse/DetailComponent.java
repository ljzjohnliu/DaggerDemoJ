package com.ljz.dagger.thirduse;

import com.ljz.dagger.DetailActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger 2 提供了一个@Singleton注解，只需要给 Component 接口、Module 中的方法加上@Singleton注解就可以了
 */
//@Singleton
@DetailActivityScope
@Component(modules = {DetailModule.class})
public interface DetailComponent {
    void inject(DetailActivity activity);
}
