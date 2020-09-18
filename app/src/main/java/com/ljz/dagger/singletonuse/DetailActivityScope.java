package com.ljz.dagger.singletonuse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 还可以自定义类似@Singleton的注解，为什么要自定义呢，可以是为了代码的可读性，在不同的场景有更明确的指向性。例如可以定义一个 DetailActivityScope 注解，替换掉上边的@Singleton，
 * 这样就能更好的体现 Book 对象在 DetailActivity 中是单例的。
 *
 * 要定义 DetailActivityScope 注解可以使用@Scope注解，具体参考@Singleton的实现
 *
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface DetailActivityScope {

}
