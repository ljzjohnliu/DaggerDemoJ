package com.ljz.dagger.basicuse;

import com.ljz.dagger.activity.MainActivity;
import com.ljz.dagger.seconduse.MainModule;

import dagger.Component;

/**
 * 创建依赖注入组件
 * 现在依赖的提供方 Cat 类有了，依赖的需求方 MainActivity 也有了，那么如何将依赖的提供方和需求方关联起来呢。
 * <p>
 * 在 Dagger 2 中也有类似的中介，那就是Component，它负责完成依赖注入的过程，我们可以叫它依赖注入组件，大致的意思就是依赖需求方需要什么类型的对象，
 * 依赖注入组件就从依赖提供方中拿到对应类型的对象，然后进行赋值。
 * <p>
 * 所以我们需要使用@Component注解来定义一个MainComponent接口,但这只是个接口，没法直接使用呀，肯定还需要具体的依赖注入逻辑的，
 * 当然，但这些工作框架会帮我们做的。我们前边说过 Dagger 2 采用了annotationProcessor技术，在项目编译时动态生成依赖注入需要的 Java 代码。
 * <p>
 * 此时我们编译项目，由于使用了@Component注解，框架会自动帮我们生成一个MainComponent接口的实现类DaggerMainComponent，这个类可以在app\build\generated\source\apt\debug\包名目录下找到。
 * <p>
 * 所以，DaggerMainComponent就是真正的依赖注入组件，最后在 MainActivity 中添加最终完成依赖注入的代码
 */
//通过如下方式将 MainModule 和 MainComponent 关联起来(modules = {MainModule.class})，这样依赖注入组件就知道从哪个依赖提供方取数据了：
@Component(modules = {MainModule.class})
public interface MainComponent {
    /**
     * inject方法的参数是依赖需求方的类型，即例子中的 MainActivity，注意不可是基类的类型。
     */
    void inject(MainActivity activity);
}