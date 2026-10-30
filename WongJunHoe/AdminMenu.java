import java.util.Scanner;
import java.util.ArrayList;

public class AdminMenu {
    private final ReportManager rm = new ReportManager();
    private final FileManager fm = new FileManager();

    public void displayMenu() {
        displayMenu(new Scanner(System.in));
    }

    public void displayMenu(Scanner sc) {
        while (true) {
            ArrayList<BankAccountRecord> bankAccounts = fm.loadBankAccounts();
            ArrayList<AccountDetails> accountProfiles = fm.loadAccounts();
            ArrayList<Transaction> transactions = fm.loadTransactions();
            ArrayList<UserCredentials> users = fm.loaduser();

            TerminalUI.clearScreen();
            TerminalUI.printCentered("╔════════════════════════════════════╗");
            TerminalUI.printCentered("║            ADMIN PANEL             ║");
            TerminalUI.printCentered("╚════════════════════════════════════╝");
            TerminalUI.printCentered("1. Freeze / Unfreeze Account");
            TerminalUI.printCentered("2. Set Savings Interest Rate");
            TerminalUI.printCentered("3. Generate Monthly Statement");
            TerminalUI.printCentered("4. View Full Account Data");
            TerminalUI.printCentered("0. Exit");

            int choice = readMenuChoice(sc, "Choice: ");
            if (choice == 0) {
                break;
            }
            if (choice < 1 || choice > 4) {
                TerminalUI.printCentered(">>> [Invalid Input] Please enter 1, 2, 3, 4, or 0.");
                TerminalUI.pauseForEnter(sc);
                continue;
            }

            if (bankAccounts.isEmpty()) {
                TerminalUI.printCentered(">>> [Error] No bank accounts found.");
                TerminalUI.pauseForEnter(sc);
                continue;
            }

            System.out.print(TerminalUI.menuPrompt("Enter Account Number (e.g. SA1000 / CA1000): "));
            String accNum = sc.nextLine().trim();

            BankAccountRecord target = findAccountByNumber(accNum, bankAccounts);

            if (target == null) {
                TerminalUI.printCentered(">>> [Error] Account not found!");
                TerminalUI.pauseForEnter(sc);
                continue;
            }

            handleChoice(choice, target, accountProfiles, transactions, bankAccounts, users, sc);
            TerminalUI.pauseForEnter(sc);
        }
    }

    private BankAccountRecord findAccountByNumber(String accountNumber, ArrayList<BankAccountRecord> bankAccounts) {
        for (BankAccountRecord account : bankAccounts) {
            if (accountNumber.equals(account.getAccountNumber())) {
                return account;
            }
        }
        return null;
    }

    private AccountDetails findProfile(BankAccountRecord bankAccount, ArrayList<AccountDetails> accountProfiles) {
        for (AccountDetails profile : accountProfiles) {
            if (bankAccount.getUserUUID() != null && bankAccount.getUserUUID().equals(profile.getUUID())) {
                return profile;
            }
        }
        for (AccountDetails profile : accountProfiles) {
            if (bankAccount.getLinkedProfileAccountNumber() != null
                    && bankAccount.getLinkedProfileAccountNumber().equals(profile.getAccountNumber())) {
                return profile;
            }
        }
        return null;
    }

    private UserCredentials findUser(BankAccountRecord bankAccount, AccountDetails profile, ArrayList<UserCredentials> users) {
        for (UserCredentials user : users) {
            if (bankAccount.getUserUUID() != null && bankAccount.getUserUUID().equals(user.getUUID())) {
                return user;
            }
        }
        if (profile != null) {
            for (UserCredentials user : users) {
                if (profile.getUsername() != null && profile.getUsername().equals(user.getUsername())) {
                    return user;
                }
            }
        }
        for (UserCredentials user : users) {
            if (bankAccount.getUsername() != null && bankAccount.getUsername().equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    private void handleChoice(int choice, BankAccountRecord target,
                              ArrayList<AccountDetails> accountProfiles,
                              ArrayList<Transaction> trans,
                              ArrayList<BankAccountRecord> bankAccounts,
                              ArrayList<UserCredentials> users,
                              Scanner sc) {
        if (choice == 1) {
            target.setFrozen(!target.isFrozen());
            fm.saveBankAccounts(bankAccounts);

            String statusLabel = target.isFrozen() ? "[ LOCKED ]" : "[ ACTIVE ]";
            String description = target.isFrozen() ? "Account has been restricted." : "Account is now functional.";
            
            System.out.println("\n-------------------------------------------------------");
            System.out.printf("  ACTION: Account Security Update\n");
            System.out.printf("  STATUS: %-15s %s\n", statusLabel, description);
            System.out.println("-------------------------------------------------------");
        }
        else if (choice == 2) {
            if ("Savings".equalsIgnoreCase(target.getAccountType())) {
                double rate = readPositiveRate(sc);
                target.setInterestRate(rate);
                fm.saveBankAccounts(bankAccounts);
                System.out.println("Interest rate updated successfully.");
            } else {
                System.out.println("This is a Current Account, no interest rate to set.");
            }
        }
        else if (choice == 3) {
            AccountDetails profile = findProfile(target, accountProfiles);
            rm.printMonthlyStatement(target, profile, trans);
        }
        else if (choice == 4) {
            AccountDetails profile = findProfile(target, accountProfiles);
            UserCredentials user = findUser(target, profile, users);
            printFullAccountData(target, profile, user);
        }
    }

    private int readMenuChoice(Scanner sc, String prompt) {
        while (true) {
            System.out.print(TerminalUI.menuPrompt(prompt));
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                TerminalUI.printCentered(">>> [Invalid Input] Please enter a number.");
            }
        }
    }

    private double readPositiveRate(Scanner sc) {
        while (true) {
            System.out.print(TerminalUI.menuPrompt("Enter new Rate: "));
            String input = sc.nextLine().trim();
            try {
                double rate = Double.parseDouble(input);
                if (rate > 0) {
                    return rate;
                }
                TerminalUI.printCentered("Interest rate must be greater than 0.");
            } catch (NumberFormatException e) {
                TerminalUI.printCentered(">>> [Invalid Input] Please enter a valid number.");
            }
        }
    }

    private void printFullAccountData(BankAccountRecord bankAccount, AccountDetails profile, UserCredentials user) {
        System.out.println("\n==============================================================");
        System.out.println("                    FULL ACCOUNT DATA");
        System.out.println("==============================================================");

        System.out.println("[Bank Account Record]");
        System.out.println("User UUID                  : " + valueOrNA(bankAccount.getUserUUID()));
        System.out.println("Linked Username            : " + valueOrNA(bankAccount.getUsername()));
        System.out.println("Linked Profile Account No. : " + valueOrNA(bankAccount.getLinkedProfileAccountNumber()));
        System.out.println("Account Number             : " + valueOrNA(bankAccount.getAccountNumber()));
        System.out.println("Account Type               : " + valueOrNA(bankAccount.getAccountType()));
        System.out.printf("Balance                    : RM %.2f%n", bankAccount.getBalance());
        System.out.printf("Interest Rate              : %.2f%%%n", bankAccount.getInterestRate());
        System.out.printf("Overdraft Limit            : RM %.2f%n", bankAccount.getOverdraftLimit());
        System.out.println("Frozen                     : " + bankAccount.isFrozen());

        System.out.println("\n[Account Profile]");
        if (profile == null) {
            System.out.println("Profile                    : Not found");
        } else {
            System.out.println("UUID                       : " + valueOrNA(profile.getUUID()));
            System.out.println("Name                       : " + valueOrNA(profile.getName()));
            System.out.println("Age                        : " + profile.getAge());
            System.out.println("Date of Birth              : " + valueOrNA(profile.getDOB()));
            System.out.println("MyKad                      : " + valueOrNA(profile.getMykad()));
            System.out.println("Gender                     : " + (profile.getGender() == '\0' ? "N/A" : profile.getGender()));
            System.out.println("Username                   : " + valueOrNA(profile.getUsername()));
            System.out.println("Profile Account Number     : " + valueOrNA(profile.getAccountNumber()));
            System.out.println("Profile Account Type       : " + valueOrNA(profile.getAccountType()));
        }

        System.out.println("\n[User Credentials]");
        if (user == null) {
            System.out.println("Credentials                : Not found");
        } else {
            System.out.println("Username                   : " + valueOrNA(user.getUsername()));
            System.out.println("Password                   : " + valueOrNA(user.getPassword()));
            System.out.println("UUID                       : " + valueOrNA(user.getUUID()));
        }

        System.out.println("==============================================================");
    }

    private String valueOrNA(String value) {
        if (value == null || value.isBlank()) {
            return "N/A";
        }
        return value;
    }
}
