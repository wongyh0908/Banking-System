import java.util.ArrayList;

public class ReportManager {
    public void printMonthlyStatement(BankAccountRecord accountRecord, AccountDetails profile,
                                      ArrayList<Transaction> transactions) {
        double totalIn = 0;
        double totalOut = 0;
        boolean hasTransaction = false;

        String customerName = accountRecord.getUsername();
        if (profile != null && profile.getName() != null && !profile.getName().isBlank()) {
            customerName = profile.getName();
        }
        if (customerName == null || customerName.isBlank()) {
            customerName = "N/A";
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     TAR DIGITAL BANK STATEMENT                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Customer Name   : %-50s ║%n", customerName.toUpperCase());
        System.out.printf("║  Account Number  : %-50s ║%n", accountRecord.getAccountNumber());
        System.out.printf("║  Account Type    : %-50s ║%n", accountRecord.getAccountType());
        System.out.printf("║  Current Balance : RM %-47.2f ║%n", accountRecord.getBalance());
        System.out.printf("║  Account Status  : %-50s ║%n", accountRecord.isFrozen() ? "FROZEN" : "ACTIVE");
        if ("Savings".equalsIgnoreCase(accountRecord.getAccountType())) {
            System.out.printf("║  Interest Rate   : %-50s ║%n", String.format("%.2f%%", accountRecord.getInterestRate()));
        }
        System.out.println("╠═════════════════════╦══════════════════════════╦═════════════════════╣");
        System.out.printf("║ %-19s ║ %-24s ║ %-19s ║%n", "Timestamp", "Type", "Amount (RM)");
        System.out.println("╠═════════════════════╬══════════════════════════╬═════════════════════╣");

        for (Transaction transaction : transactions) {
            if (!accountRecord.getAccountNumber().equals(transaction.getAccountNumber())) {
                continue;
            }
            if (accountRecord.getUserUUID() != null
                    && transaction.getUserUUID() != null
                    && !accountRecord.getUserUUID().equals(transaction.getUserUUID())) {
                continue;
            }

            hasTransaction = true;
            System.out.printf("║ %-19s ║ %-24s ║ RM %16.2f ║%n",
                    transaction.getTimestamp(),
                    transaction.getType(),
                    transaction.getAmount());

            if ("Deposit".equalsIgnoreCase(transaction.getType())
                    || "Monthly Interest".equalsIgnoreCase(transaction.getType())) {
                totalIn += transaction.getAmount();
            } else {
                totalOut += transaction.getAmount();
            }
        }

        if (!hasTransaction) {
            System.out.printf("║ %-66s ║%n", "No transaction records for this account.");
        }

        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Total Inflows   : RM %-47.2f ║%n", totalIn);
        System.out.printf("║  Total Outflows  : RM %-47.2f ║%n", totalOut);
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }
}
