# DaggerDemoJ

## 3.dependencies
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
    
    
## 4.@Subcomponent
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