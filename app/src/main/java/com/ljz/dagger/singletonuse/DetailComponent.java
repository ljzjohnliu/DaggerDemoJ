package com.ljz.dagger.singletonuse;

import com.ljz.dagger.activity.DetailActivity;
import com.ljz.dagger.common.CommonComponent;
import com.ljz.dagger.common.CommonScope;
import com.ljz.dagger.subcompuse.MySubComponent;
import com.ljz.dagger.subcompuse.SubModule;

import dagger.Component;

/**
 * Dagger 2 提供了一个@Singleton注解，只需要给 Component 接口、Module 中的方法加上@Singleton注解就可以了
 */
//@Singleton
@CommonScope
@Component(modules = {DetailModule.class}, dependencies = {CommonComponent.class})
public interface DetailComponent {
    void inject(DetailActivity activity);

    // 定义返回子组件的方法，参数为子组件需要的module
    MySubComponent getSubComponent(SubModule module);
}
