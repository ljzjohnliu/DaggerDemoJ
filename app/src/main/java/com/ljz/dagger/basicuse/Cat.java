package com.ljz.dagger.basicuse;

import javax.inject.Inject;

/**
 * 创建提供依赖的类
 * 定义了一个 Cat 类，并在它的无参构造函数上使用了 @Inject注解，告诉 Dagger 2 这个无参构造函数可以被用来创建 Cat 对象，即依赖的提供方
 */
public class Cat {

    @Inject
    public Cat() {

    }

    @Override
    public String toString() {
        return "喵星人来了!";
    }
}
