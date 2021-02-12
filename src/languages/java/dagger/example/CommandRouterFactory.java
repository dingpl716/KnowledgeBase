@Component(modules = {HelloWorldModule.class, SystemOutModule.class})
interface CommandRouterFactory {
  CommandRouter router();
}
