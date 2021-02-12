final class CommandRouter {

  /** 
   *  这是只支持一个Command的方法 
   *
   *  private final Map<String, Command> commands = Collections.emptyMap();
   *
   *  // Used to tell Dagger how to create instance of this class.
   *  @Inject
   *  CommandRouter(Command command) {
   *    commands.put(command.key(), command);
   *  }
   */

  // 支持多个Command的方法
  private final Map<String, Command> commands;

  @Inject
  CommandRouter(Map<String, Command> commands) {
    // This map contains:
    // "hello" -> HelloWorldCommand
    // "login" -> LoginCommand
    this.commands = commands;
  }

  Status route(String input) {
    List<String> splitInput = split(input);
    if (splitInput.isEmpty()) {
      return invalidCommand(input);
    }

    String commandKey = splitInput.get(0);
    Command command = commands.get(commandKey);
    if (command == null) {
      return invalidCommand(input);
    }

    Status status =
        command.handleInput(splitInput.subList(1, splitInput.size()));
    if (status == Status.INVALID) {
      System.out.println(commandKey + ": invalid arguments");
    }
    return status;
  }

  private Status invalidCommand(String input) {
    System.out.println(
        String.format("couldn't understand \"%s\". please try again.", input));
    return Status.INVALID;
  }

  // Split on whitespace
  private static List<String> split(String string) {  ...  }
}
