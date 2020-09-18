package com.ljz.dagger.qualifieruse;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * 前边我们的 Cat 类并没有带参的构造函数，可以直接在其无参的构造函数使用@Inject注解，进而当做依赖提供方来提供对象。
 * 但实际的情况可能并没有这么简单，可能构造函数需要参数，或者类是第三方提供的我们无法修改等，导致我们无法使用@Inject，这些情况下就需要我们自定义依赖提供方了。
 * <p>
 * 在 Dagger 2 中，如果一个类使用了@Module注解，那么这个类就可以用来提供依赖对象：
 * <p>
 * 如果要让 MainModule 类可以提供 Flower 对象，可以做如下修改：
 * 即声明了一个provideRedFlower()方法，并使用了@Provides注解，这样在底层 MainModule 类就可以调用provideRedRose()方法，来提供对应的依赖对象了。
 *
 * 如果 MainActivity 中需要注入多个 Flower 对象，例如红玫瑰、白玫瑰、蓝玫瑰，我们必然需要在 MainModule 中提供多个类似provideXXXRose()的方法，但是问题来了，
 * 我们需要将 MainModule 中多个provide 方法和 MainActivity 中声明的多个 Flower 变量对应起来呢，否则 Dagger 2 也不知道把那个 provide 方法的返回值赋给那个 Flower 变量。
 *
 * 为了解决这个问题 Dagger 2 提供了@Named注解，该注解需要设置一个属性值来区分类型。来看如何使用，首先给 MainModule 中的方法加上@Named：
 *
 */
@Module
public class MainModule {

    @Named("red")
    @Provides
    public Flower provideRedRose() {
        return new Flower("玫瑰", "红色");
    }

    @Named("white")
    @Provides
    public Flower provideWhiteRose() {
        return new Flower("玫瑰", "白色");
    }

    @QualifierBlue
    @Provides
    public Flower provideBlueRose() {
        return new Flower("玫瑰", "蓝色");
    }

}
