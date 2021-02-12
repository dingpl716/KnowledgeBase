@Module
abstract class LoginCommandModule {
  @Binds
  @IntoMap
  @StringKey("login")
  abstract Command loginCommand(LoginCommand command);
}
