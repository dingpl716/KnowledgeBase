# Dagger

[ATM Example](https://github.com/google/dagger/tree/master/java/dagger/example/atm)

## `@Component`

- 该Annotation用来标注一个`interface`, Dagger会自动的为这个被标注的`interface`生成代码。
- 这个被标注的`interface`其实是一个`Factory`，用来提供Dependency Graph上的**顶层组件所直接依赖的组件**。
- 其原因是因为，顶层元素的创建，往往是通过手工调用构造函数完成的，故而Dagger无法为你生成这部分代码。所以我们必须显示的去调用这个`Factory`里面的函数去获取顶层元素所依赖的组件。比如在下面的例子中所示，`Server`是最顶层的元素了，我们需要在`main`函数里面构造并启动它，那么我们只能用这种手动的方式去实现。
  ```java
  @Component
  interface ServerComponentFactory {
      int getPort();
      Config getConfig();
  }

  public static void main(String[] args) {
    ServerComponentFactory serverComponentFactory = DaggerServerComponentFactory.create();

    Server server = new Server(
        serverComponentFactory.getPort(),
        serverComponentFactory.getConfig()
    )

    server.start();
  }
  ```

## `@Inject`
- 该Annotation用来标注一个构造函数或者一个字段。
- 如果`Class A`的构造函数被标注，那么Dagger会用这个构造函数来创建`Class A`的实例。**此时`Class A`即是别的组件的依赖，同时又依赖于另外一些组件**
- 如果`Class A`的一个字段被标注，那么Dagger不会创建这个`Class A`的实例，而是会为相应的字段注入一个实例。**此时`Class A`的字段依赖于别的组件**


## `@Module`
- 用来标注一个class或者interface
- `@Module`是一个集合，在这个集合里面的所有元素都是用来告诉Dagger如何`提供`依赖的。


### `@Binds`
- 只能在`@Module`里面使用
- 只能标注`abstract method`
- 用来告诉Dagger如何提供一个依赖。
- 方法的参数是Dagger**已经知道**如何创建的一个class，方法的返回值是Dagger**不知道**如何创建的一个class
- 比如在下面的例子中`HelloWorldCommand`是一个Dagger以及知道如何创建的实例，而`Command`是一个Interface，是Dagger不知道如何创建的依赖。
  ```java
  @Module
  abstract class HelloWorldModule {
    @Binds
    abstract Command helloWorldCommand(HelloWorldCommand command);
  }
  ```

### `@Provides`
- 只能用在`@Module`里面
- 只能用来标注`concrete method`
- 用来告诉Dagger如何提供一个依赖

### `@IntoMap` && `@StringKey`, `@IntKey`, `@LongKey`, `@ClassKey`
- 只能用在`@Module`里面
- 可以和`@Binds`，`@Provides` 合起来用
- 在下面的例子中，因为用`@IntoMap` && `@StringKey`标注，所以一旦有什么地方依赖于一个`Map<String, Command>`的时候，Dagger就知道如何提供依赖。
  ```java
  // Supply
  @Module
  abstract class HelloWorldModule {
    @Binds
    @IntoMap
    @StringKey("hello")
    abstract Command helloWorldCommand(HelloWorldCommand command);
  }

  // Supply
  @Module
  abstract class LoginCommandModule {
    @Binds
    @IntoMap
    @StringKey("login")
    abstract Command loginCommand(LoginCommand command);
  }

  // Demand
  @Inject
  CommandRouter(Map<String, Command> commands) {
    // This map contains:
    // "hello" -> HelloWorldCommand
    // "login" -> LoginCommand
    this.commands = commands;
  }
  ```

### `@IntoSet`
- 和`@IntoMap`类似，提供一个`Set<ReturnType>`作为依赖。

## Conclusions
- 当某个组件依赖于一个interface的时候，我们将面对一个“一对多”的问题，即一个Interface对应多个实现。那么此时Dagger该如何解决依赖注入的问题？如何选取到底该用哪个interface的实现？


| Supply     | Demand       |
| ---------- | ------------ |
| @Component | @Inject 字段  |
| @Module    |              |
| @Binds     |              |
| @Provides  |              |
