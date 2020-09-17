package com.ljz.dagger.basic;

/**
 * 依赖注入通俗的理解就是，当A类需要引用B类的对象时，将B类的对象传入A类的过程就是依赖注入。依赖注入最重要的作用就是解耦，降低类之间的耦合，保证代码的健壮性、可维护性、可扩展性。
 *
 * 常用的实现方式有构造函数、set方法、实现接口等。例如：
 *
 */
public class A {
    B b;

    public A(B b) {
        this.b = b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
