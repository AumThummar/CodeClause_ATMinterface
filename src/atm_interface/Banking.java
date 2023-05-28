package atm_interface;

import java.util.Scanner;

public class Banking {
    private FileHandler fileHandler;

    public Banking() {
        fileHandler = new FileHandler();
    }

    public void authenticateUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Authenticate user password
        if (fileHandler.authenticateAccount(accountNumber, password)) {
            performOperations(accountNumber);
        } else {
            System.out.println("Authentication failed! Access denied.");
        }
    }

    private void performOperations(String accountNumber) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
        	System.out.println("\n______________________________\n");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer to Another Account");
            System.out.println("4. Transaction History");
            System.out.println("5. Account Information");
            System.out.println("6. View Balance");
            System.out.println("7. Go Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the remaining newline character
                    fileHandler.deposit(accountNumber, depositAmount);
                    System.out.println("Amount deposited successfully!");
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the remaining newline character
                    if (fileHandler.withdraw(accountNumber, withdrawAmount)) {
                        System.out.println("Amount withdrawn successfully!");
                    } else {
                        System.out.println("Insufficient balance!");
                    }
                    break;
                case 3:
                    System.out.print("Enter recipient's account number: ");
                    String recipientAccountNumber = scanner.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the remaining newline character
                    if (fileHandler.transfer(accountNumber, recipientAccountNumber, transferAmount)) {
                        System.out.println("Amount transferred successfully!");
                    } else {
                        System.out.println("Transfer failed! Invalid recipient account number or insufficient balance.");
                    }
                    break;
                case 4:
                    fileHandler.displayTransactionHistory(accountNumber);
                    break;
                case 5:
                    fileHandler.displayAccountInformation(accountNumber);
                    break;
                case 6:
                    fileHandler.displayBalance(accountNumber);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
