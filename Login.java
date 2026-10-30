import java.util.Scanner;
import java.util.ArrayList;

public class Login {
  private static final String ADMIN_USERNAME = "TARCADMIN";
  private static final String ADMIN_PASSWORD = "TARCADMIN";
  private String username;
  private String password;
  FileManager fm = new FileManager();
  Scanner scanner;
  ArrayList<UserCredentials> users = new ArrayList<>();
  FirstTimeSetup fts;

  public Login() {
    this(new Scanner(System.in));
  }

  public Login(Scanner scanner) {
    this.scanner = scanner;
    this.fts = new FirstTimeSetup(scanner);
  }

  public void loginLoop() {
    users = fm.loaduser();

    while (true) {
      System.out.print(TerminalUI.menuPrompt("Enter your username: "));
      username = scanner.nextLine();
      System.out.print(TerminalUI.menuPrompt("Enter your password: "));
      password = scanner.nextLine();

      for (UserCredentials user : users) {
        if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
          TerminalUI.printCentered("Login successful!");
          if (isAdminCredential(user)) {
            TerminalUI.printCentered("Admin mode activated.");
            AdminMenu adminMenu = new AdminMenu();
            adminMenu.displayMenu(scanner);
          } else {
            fts.setup(user);
          }
          return;
        }
      }
      TerminalUI.printCentered("Username or password is wrong.");
    }
  }

  private boolean isAdminCredential(UserCredentials user) {
    return ADMIN_USERNAME.equals(user.getUsername()) && ADMIN_PASSWORD.equals(user.getPassword());
  }
}
