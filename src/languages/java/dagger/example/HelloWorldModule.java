@Module
abstract class HelloWorldModule {
  @Binds
  @IntoMap
  @StringKey("hello")
  abstract Command helloWorldCommand(HelloWorldCommand command);
}
