final class HelloWorldCommand implements Command {
  private final Outputter outputter;

  // Used to tell Dagger how to create instance of this class.
  @Inject
  HelloWorldCommand(Outputter outputter) {
    this.outputter = outputter;
  }

  @Override
  public String key() {
    return "hello";
  }

  @Override
  public Status handleInput(List<String> input) {
    if (!input.isEmpty()) {
      return Status.INVALID;
    }
    outputter.output("world!");
    return Status.HANDLED;
  }
}
