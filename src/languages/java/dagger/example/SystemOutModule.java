@Module
abstract class SystemOutModule {
  @Provides
  static Outputter textOutputter() {
    return System.out::println;
  }
}
