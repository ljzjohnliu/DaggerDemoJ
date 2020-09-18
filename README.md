# DaggerDemoJ

## 一、依赖注入
    依赖注入通俗的理解就是，当A类需要引用B类的对象时，将B类的对象传入A类的过程就是依赖注入。依赖注入最重要的作用就是解耦，降低类之间的耦合，保证代码的健壮性、可维护性、可扩展性。
    
    常用的实现方式有构造函数、set方法、实现接口等。例如：
    
    // 通过构造函数
    public class A {
        B b;
        public A(B b) {
            this.b = b;
        }
    }
    
    // 通过set方法
    public class A {
        B b;
        public void setB(B b) {
            this.b = b;
        }
    }
    但这些方式并不是足够好的，如果有几百个类需要引用B类的对象，意味着我们需要写几百个以B类为参数的构造函数、或者set方法，所以随着使用规模的增加会产生大量的模板代码，这对后期的维护和修改带来困难。而且在 Activity、Fragment 这样的类中，用上边这些方式似乎很难实现依赖注入。
    
    面对这些问题，更好的 Dagger 2 来了

## 二、Dagger2使用

### 1.基本使用
    这里以 Android 中的用法为例，通过一个小例子来学会用 Dagger 2 完成依赖注入的基本流程。
    
    (1)、添加依赖库
    在 app module 中的 build.gradle 中添加 Dagger 2 的依赖，有两部分 Dagger 2 的核心库，以及注解处理器：
    
    implementation 'com.google.dagger:dagger:2.19'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.19'
    
    (2)、创建提供依赖的类
    public class Cat {
        @Inject
        public Cat() {
        }
    
        @Override
        public String toString() {
            return "喵星人来了!";
        }
    }
    我们定义了一个 Cat 类，并在它的无参构造函数上使用了 @Inject注解，告诉 Dagger 2 这个无参构造函数可以被用来创建 Cat 对象，即依赖的提供方，至于原理后边再说。
    
    (3)、创建使用依赖对象的类
    public class MainActivity extends AppCompatActivity {
        @Inject
        Cat cat;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
     
            Log.e("cat", cat.toString());
        }
    }
    在 MainActivity 中创建了一个 cat变量，并加上了 @Inject注解，来告诉 Dagger2 你要为cat赋值，即依赖注入。所以 MainActivity 就是依赖的需求方。
    
    (4)、创建依赖注入组件
    现在依赖的提供方 Cat 类有了，依赖的需求方 MainActivity 也有了，那么如何将依赖的提供方和需求方关联起来呢。类似于我们日常的购物，卖家和买家需要通过电商平台的中介来完成供需信息的交换，完成最终的交易。
    
    在 Dagger 2 中也有类似的中介，那就是Component，它负责完成依赖注入的过程，我们可以叫它依赖注入组件，大致的意思就是依赖需求方需要什么类型的对象，依赖注入组件就从依赖提供方中拿到对应类型的对象，
    然后进行赋值。
    
    所以我们需要使用@Component注解来定义一个MainComponent接口：
    
    @Component
    public interface MainComponent {
        void inject(MainActivity activity);
    }
    inject方法的参数是依赖需求方的类型，即例子中的 MainActivity，注意不可是基类的类型。
    
    但这只是个接口，没法直接使用呀，肯定还需要具体的依赖注入逻辑的，当然，但这些工作框架会帮我们做的。我们前边说过 Dagger 2 采用了annotationProcessor技术，在项目编译时动态生成依赖注入需要的 Java 代码。
    
    此时我们编译项目，由于使用了@Component注解，框架会自动帮我们生成一个MainComponent接口的实现类DaggerMainComponent，这个类可以在app\build\generated\source\apt\debug\包名目录下找到。
    
    所以，DaggerMainComponent就是真正的依赖注入组件，最后在 MainActivity 中添加最终完成依赖注入的代码：
    
    public class MainActivity extends AppCompatActivity {
        @Inject
        Cat cat;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
     
            DaggerMainComponent.builder()
                    .build()
                    .inject(this);
    
            Log.e("cat", cat.toString());
        }
    然后运行项目，就可以看到如下的 Log，说明依赖注入成功了。
    
### 2.通用的依赖提供方
    前边我们的 Cat 类并没有带参的构造函数，可以直接在其无参的构造函数使用@Inject注解，进而当做依赖提供方来提供对象。
    但实际的情况可能并没有这么简单，可能构造函数需要参数，或者类是第三方提供的我们无法修改等，导致我们无法使用@Inject，
    这些情况下就需要我们自定义依赖提供方了。
    
    在 Dagger 2 中，如果一个类使用了@Module注解，那么这个类就可以用来提供依赖对象：
    
    @Module
    public class MainModule {
    }
    如果现在有如下的 Flower 类：
    
    public class Flower {
        private String name;
        private String color;
    
        public Flower(String name, String color) {
            this.name = name;
            this.color = color;
        }
    
        @Override
        public String toString() {
            return "Flower{" + "name='" + name + "', color='" + color + "'}";
        }
    }
    
    如果要让 MainModule 类可以提供 Flower 对象，可以做如下修改：
    
    @Module
    public class MainModule {
        @Provides
        public Flower provideRedRose() {
            return new Flower("玫瑰", "红色");
        }
    }
    即声明了一个provideRedFlower()方法，并使用了@Provides注解，这样在底层 MainModule 类就可以调用provideRedRose()方法，来提供对应的依赖对象了。
    
    接下来通过如下方式将 MainModule 和 MainComponent 关联起来，这样依赖注入组件就知道从哪个依赖提供方取数据了：
    
    @Component(modules = {MainModule.class})
    public interface MainComponent {
        void inject(MainActivity activity);
    }
    编译项目后修改依赖注入配置代码：
    
    public class MainActivity extends AppCompatActivity {
        @Inject
        Flower flower;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    
            DaggerMainComponent.builder()
                     // 设置 MainModule 对象
                    .mainModule(new MainModule())
                    .build()
                    .inject(this);
    
            Log.e("flower", flower.toString());
        }
    }
    运行后可以看到如下 Log 信息：   
    
    所以通过 MainModule 类来提供依赖对象，更加的通用，这样我们也可以去掉 Cat 类中的 @Inject注解，进而通过 MainModule 类来提供依赖对象，更容易维护。
    相信到这里你已经掌握了 Dagger 2 的基本使用流程，知道了@Inject、@Component、@Module、@Provides这几个注解的用途。
    
### 3.@Named、@Qualifier
    试想一下，如果 MainActivity 中需要注入多个 Flower 对象，例如红玫瑰、白玫瑰、蓝玫瑰，我们必然需要在 MainModule 中提供多个类似provideXXXRose()的方法，
    但是问题来了，我们需要将 MainModule 中多个provide 方法和 MainActivity 中声明的多个 Flower 变量对应起来呢，否则 Dagger 2 也不知道把那个 provide 方法的返回值赋给那个 Flower 变量。
    
    为了解决这个问题 Dagger 2 提供了@Named注解，该注解需要设置一个属性值来区分类型。来看如何使用，首先给 MainModule 中的方法加上@Named：
    
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
        
    @Named("red")就表示红玫瑰，@Named("white")就表示白玫瑰。我们还需要给 MainActivity 中的 Flower 变量加上对应的注解，这样就可以把它们关联起来了：
    
    public class MainActivity extends AppCompatActivity {
        @Named("red")
        @Inject
        Flower flower1;
    
        @Named("white")
        @Inject
        Flower flower2;
    }
    可能有人要说了，@Named需要属性值，或者不想使用 Named 这个名字，也很简单，那就需要使用@Qualifier自定义注解，怎么自定呢，先看@Named是如何实现的:
    
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public @interface Named {
        String value() default "";
    }
    
    照猫画虎，我们可以自定义一个@QualifierBlue注解，来区分蓝玫瑰：
    
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface QualifierBlue {
    }
    用法也类似：
    
    @QualifierBlue
    @Provides
    public Flower provideBlueRose() {
        return new Flower("玫瑰", "蓝色");
    }
    
    @QualifierBlue
    @Inject
    Flower flower3;
    
### 4.@Singleton、@Scope
    现在有一个 Book 类，在 Activity 中需要注入多个 Book 对象，但要求注入的是同一个 Book 对象，按照下面的写法可以实现吗？
    
    @Module
    public class DetailModule {
        @Provides
        public Book provideBook() {
            return new Book("Kotlin 指南", 66.8f);
        }
    }
    @Component(modules = {DetailModule.class})
    public interface DetailComponent {
        void inject(DetailActivity activity);
    }
    public class DetailActivity extends AppCompatActivity {
        @Inject
        Book book1;
    
        @Inject
        Book book2;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detatil);
    
            DaggerDetailComponent.builder()
                    .detailModule(new DetailModule())
                    .build()
                    .inject(this);
    
            Log.e("book1", book1.toString());
            Log.e("book2", book2.toString());
        }
    }
    观察输入的 Log：    
    可以看到 book1、book2 并不是同一个对象，为了实现这样的功能， Dagger 2 提供了一个@Singleton注解，只需要给 Component 接口、Module 中的方法加上@Singleton注解就可以了：
    @Singleton
    @Component(modules = {DetailModule.class})
    public interface DetailComponent {
        void inject(DetailActivity activity);
    }
    @Module
    public class DetailModule {
        @Singleton
        @Provides
        public Book provideBook() {
            return new Book("Kotlin 指南", 66.8f);
        }
    }
    再运行看效果：
    我们的目标实现了，所以@Singleton注解注解可以实现“单例”的效果，但这个单例只是局部的，只在单个 Activity 有效，就是说如果是两个 Activity 想得到同一个对象，就失效了。关于这个问题我们后边再讨论。
    同时我们还可以自定义类似@Singleton的注解，为什么要自定义呢，可以是为了代码的可读性，在不同的场景有更明确的指向性。例如可以定义一个 DetailActivityScope 注解，
    替换掉上边的@Singleton，这样就能更好的体现 Book 对象在 DetailActivity 中是单例的。
    要定义 DetailActivityScope 注解可以使用@Scope注解，具体参考@Singleton的实现：
    
    @Scope
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DetailActivityScope {
    }
    现在就可以用@DetailActivityScope替换掉上边的@Singleton，最终的效果是一样的。
    
    
### 5.dependencies
    我们现在看上边遗留的问题，通过 Dagger 2 如何实现全局单例，比如多个 Activity 如何共享同一个对象。在多个 Module 类里定义相同的 provide 方法是行不通的，例如：
    @Singleton
    @Provides
    public Book provideBook() {
        return new Book("Flutter 指南", 68.8f);
        }
    因为在多个 Activity 中 Component、 Module 类都已经不是同一个了，自然不能保证 Book 是同一个了。
    Dagger 2 的@Component注解可以设置dependencies属性，来依赖其它的 Component，这样我们可以定义一套公共的 Component + Module，让需要的 Component 来依赖公共的 Component，这样问题就解决了。
    具体如何做，接下来详细说。
    
    首先定义公共的 Module 和 Component：
    @Module
    public class CommonModule {
        @Singleton
        @Provides
        public Book provideBook() {
            return new Book("Flutter 指南", 68.8f);
        }
    }
    
    @Singleton
    @Component(modules = {CommonModule.class})
    public interface CommonComponent {
        Book provideBook();
    }
    
    都使用了@Singleton注解。注意，这个 CommonComponent 和我们之前的不太一样，并没有inject方法，可以这样理解，CommonComponent 并不是直接用来对应Activity 完成以依赖注入的，
    而是告诉依赖它的 Component 我可以给你提供什么依赖对象，所以这里定义了一个provideBook()方法，和 CommonModule 中的方法对应。
    
    接下来让 MainComponent、ShareComponent 依赖 CommonComponent：
    
    @CommonScope
    @Component(modules = {MainModule.class}, dependencies = {CommonComponent.class})
    public interface MainComponent {
        void inject(MainActivity activity);
    }
    @CommonScope
    @Component(dependencies = {CommonComponent.class})
    public interface ShareComponent {
        void inject(ShareActivity activity);
    }
    编译项目后，就可以进行依赖注入的配置了：
    
    DaggerMainComponent.builder()
                .mainModule(new MainModule())
                .commonComponent(???)
                .build()
                .inject(this);
                
    问题来了，commonComponent()方法需要一个 CommonComponent 对象，同理在 ShareActivity 中也需要一个 CommonComponent 对象，这里我们要保证两个 CommonComponent 对象是同一个，
    原因前边已经说了，不同的 Component 对象自然无法提供相同的依赖对象。要获得相同的 CommonComponent 对象，简单的做法可以通过 Application 来实现，因为在单进程的应用中，
    Application 的只被初始化一次，可以保证唯一性：
    
    
    public class App extends Application {
    private CommonComponent commonComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        commonComponent = DaggerCommonComponent.builder().commonModule(new CommonModule()).build();
    }

    public CommonComponent getCommonComponent() {
        return commonComponent;
     }
    }
    别忘了在 AndroidManifest 中配置自定义的 Application。
    所以 CommonComponent 对象的配置可以这样写：
    commonComponent(((App) getApplication()).getCommonComponent())
    
    我们在两个 Activity 中分别依赖一个 Book 对象，并通过 Log 输出：
    是我们想要的结果。
    
    
## 6.@Subcomponent
    @Subcomponent是用来实现一个子依赖注入组件的，就是说使用@Subcomponent注解的 Component 可以有一个父依赖注入组件，
    但这种父子关系并不是通过传统的继承方式实现的。自然的，子依赖注入组件会拥有父依赖注入组件的功能。
    
    首先定义一个使用@Subcomponent注解的依赖注入组件接口：
    
    @Subcomponent(modules = {SubModule.class})
    public interface MySubComponent {
        void inject(SubActivity activity);
    }
    
    它依赖的 SubModule 为：
    
    @Module
    public class SubModule {
        @Provides
        public Flower provideFlower() {
            return new Flower("腊梅", "红色");
        }
    }
    
    如果我们想让 DetailComponent 做为 MySubComponent 的父组件，则需要在 DetailComponent 中定义一个返回 MySubComponent 的方法，方法参数为其依赖的 Module 类型：
    
    @DetailActivityScope
    @Component(modules = {DetailModule.class})
    public interface DetailComponent {
        void inject(DetailActivity activity);
    
        // 定义返回子组件的方法，参数为子组件需要的module
        MySubComponent getSubComponent(SubModule module);
    }
    到这里我们的子组件就实现完成了，如何使用呢？子依赖注入注入组件是不能单独直接使用的，因为编译后并不会生成类似DaggerMySubComponent的辅助类，所以需要通过父组件来获取，这也是我们需要在父组件中定义返回子组件方法的原因。具体的用法如下：
    
    public class SubActivity extends AppCompatActivity {
        @Inject
        Book book;
    
        @Inject
        Flower flower;
    
        public static void start(Context context) {
            context.startActivity(new Intent(context, SubActivity.class));
        }
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sub);
            // 创建父组件对象
            DetailComponent detailComponent = DaggerDetailComponent.builder().detailModule(new DetailModule()).build();
            // 得到子组件，并完成依赖注入
            detailComponent.getSubComponent(new SubModule()).inject(this);
    
            Log.e("SubActivity-book", book.toString());
            Log.e("flower", flower.toString());
        }
    }
    我们并没有在 SubModule 中定义提供 Flowerd 对象的方法，但是同过这种“继承”，MySubComponent 就可以提供 Flower 对象了.