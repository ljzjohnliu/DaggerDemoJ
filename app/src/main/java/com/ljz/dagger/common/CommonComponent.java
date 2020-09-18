package com.ljz.dagger.common;

import com.ljz.dagger.thirduse.Book;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 现在看上边遗留的问题，通过 Dagger 2 如何实现全局单例，比如多个 Activity 如何共享同一个对象。在多个 Module 类里定义相同的 provide 方法是行不通的，例如：
 *
 * @Singleton
 * @Provides
 * public Book provideBook() {
 *     return new Book("Flutter 指南", 68.8f);
 * }
 * 因为在多个 Activity 中 Component、 Module 类都已经不是同一个了，自然不能保证 Book 是同一个了。
 *
 * Dagger 2 的@Component注解可以设置dependencies属性，来依赖其它的 Component，这样我们可以定义一套公共的 Component + Module，让需要的 Component 来依赖公共的 Component，这样问题就解决了
 *
 * 这个 CommonComponent 和我们之前的不太一样，并没有inject方法，可以这样理解，CommonComponent 并不是直接用来对应Activity 完成以依赖注入的，而是告诉依赖它的 Component 我可以给你提供什么依赖对象，
 * 所以这里定义了一个provideBook()方法，和 CommonModule 中的方法对应。
 *
 */
@Singleton
@Component(modules = {CommonModule.class})
public interface CommonComponent {
    Book provideBook();
}