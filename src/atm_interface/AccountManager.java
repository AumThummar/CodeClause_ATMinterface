package atm_interface;

import java.util.Scanner;

public class AccountManager {
    private FileHandler fileHandler;

    public AccountManager() {
        fileHandler = new FileHandler();
    }

    public void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume the remaining newline character
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter sex: ");
        String sex = scanner.nextLine();
        System.out.print("Enter account type: ");
        String accountType = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String accountNumber = generateAccountNumber();
        
        double balance = 0.0;

        // Save account details to file
        fileHandler.storeAccountDetails(accountNumber, accountHolderName, age, phoneNumber, sex, accountType, password, balance);

        System.out.println("Account created successfully!");
        System.out.println("Account Number: " + accountNumber);
    }

    private String generateAccountNumber() {
        // Generate a random 5-digit account number
        int accountNumber = (int) (Math.random() * 90000) + 10000;
        return String.valueOf(accountNumber);
    }

    public void deleteAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Authenticate user password
        if (fileHandler.authenticateAccount(accountNumber, password)) {
            // Delete account details from file
            fileHandler.deleteAccountDetails(accountNumber);
            System.out.println("Account deleted successfully!");
        } else {
            System.out.println("Authentication failed! Account not deleted.");
        }
    }

    public void editAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Authenticate user password
        if (fileHandler.authenticateAccount(accountNumber, password)) {
            System.out.println("1. Change Account Holder Name");
            System.out.println("2. Change Age");
            System.out.println("3. Change Phone Number");
            System.out.println("4. Change Sex");
            System.out.println("5. Change Account Type");
            System.out.println("6. Change Password");
            System.out.println("7. Go Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter new account holder name: ");
                    String newAccountHolderName = scanner.nextLine();
                    fileHandler.updateAccountHolderName(accountNumber, newAccountHolderName);
                    System.out.println("Account holder name updated successfully!");
                    break;
                case 2:
                    System.out.print("Enter new age: ");
                    int newAge = scanner.nextInt();
                    scanner.nextLine(); // Consume the remaining newline character
                    fileHandler.updateAge(accountNumber, newAge);
                    System.out.println("Age updated successfully!");
                    break;
                case 3:
                    System.out.print("Enter new phone number: ");
                    String newPhoneNumber = scanner.nextLine();
                    fileHandler.updatePhoneNumber(accountNumber, newPhoneNumber);
                    System.out.println("Phone number updated successfully!");
                    break;
                case 4:
                    System.out.print("Enter new sex: ");
                    String newSex = scanner.nextLine();
                    fileHandler.updateSex(accountNumber, newSex);
                    System.out.println("Sex updated successfully!");
                    break;
                case 5:
                    System.out.print("Enter new account type: ");
                    String newAccountType = scanner.nextLine();
                    fileHandler.updateAccountType(accountNumber, newAccountType);
                    System.out.println("Account type updated successfully!");
                    break;
                case 6:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    fileHandler.updatePassword(accountNumber, newPassword);
                    System.out.println("Password updated successfully!");
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } else {
            System.out.println("Authentication failed! Account not edited.");
        }
    }
}
