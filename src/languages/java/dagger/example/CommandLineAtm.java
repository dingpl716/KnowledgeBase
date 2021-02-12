// class CommandLineAtm {
//   public static void main(String[] args) {
//     Scanner scanner = new Scanner(System.in);
//     CommandRouter commandRouter = new CommandRouter();

//     while (scanner.hasNextLine()) {
//       commandRouter.route(scanner.nextLine());
//     }
//   }
// }

class CommandLineAtm {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    CommandRouterFactory commandRouterFactory = DaggerCommandRouterFactory.create();
    CommandRouter commandRouter = commandRouterFactory.router();

    while (scanner.hasNextLine()) {
      commandRouter.route(scanner.nextLine());
    }
  }
}
