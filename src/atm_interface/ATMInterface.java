package atm_interface;

import java.util.Scanner;

public class ATMInterface {
    private AccountManager accountManager;
    private Banking banking;
    private FileHandler fileHandler;

    public ATMInterface() {
        accountManager = new AccountManager();
        banking = new Banking();
        fileHandler = new FileHandler();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
        	System.out.println("\n\n==========Welcome to ATM program by Aum Thummar==========\n\n");
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Edit Account");
            System.out.println("4. Banking");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    accountManager.createAccount();
                    break;
                case 2:
                    accountManager.deleteAccount();
                    break;
                case 3:
                    accountManager.editAccount();
                    break;
                case 4:
                    banking.authenticateUser();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
