package com.ljz.dagger.seconduse;

import dagger.Module;
import dagger.Provides;

/**
 * 前边我们的 Cat 类并没有带参的构造函数，可以直接在其无参的构造函数使用@Inject注解，进而当做依赖提供方来提供对象。
 * 但实际的情况可能并没有这么简单，可能构造函数需要参数，或者类是第三方提供的我们无法修改等，导致我们无法使用@Inject，这些情况下就需要我们自定义依赖提供方了。
 *
 * 在 Dagger 2 中，如果一个类使用了@Module注解，那么这个类就可以用来提供依赖对象：
 *
 * 如果要让 MainModule 类可以提供 Flower 对象，可以做如下修改：
 * 即声明了一个provideRedFlower()方法，并使用了@Provides注解，这样在底层 MainModule 类就可以调用provideRedRose()方法，来提供对应的依赖对象了。
 *
 */
@Module
public class MainModule {

    @Provides
    public Flower provideRedRose() {
        return new Flower("玫瑰", "红色");
    }
}
