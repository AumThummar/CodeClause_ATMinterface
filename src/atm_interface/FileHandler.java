package atm_interface;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class FileHandler {
    private final String ACCOUNTS_FILE = "accounts.txt";
    private final String TRANSACTION_FILE = "transaction_history.txt";

    public FileHandler() {
        // Create the accounts file if it doesn't exist
        File accountsFile = new File(ACCOUNTS_FILE);
        if (!accountsFile.exists()) {
            try {
                accountsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create the transaction history file if it doesn't exist
        File transactionFile = new File(TRANSACTION_FILE);
        if (!transactionFile.exists()) {
            try {
                transactionFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeAccountDetails(String accountNumber, String accountHolderName, int age, String phoneNumber, String sex,
                                    String accountType, String password, double balance) {
        try (FileWriter fileWriter = new FileWriter(ACCOUNTS_FILE, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(accountNumber + "," + accountHolderName + "," + age + "," + phoneNumber + "," +
                    sex + "," + accountType + "," + password + "," + balance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateAccount(String accountNumber, String password) {
        try (Scanner scanner = new Scanner(new File(ACCOUNTS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] accountDetails = line.split(",");

                if (accountDetails[0].equals(accountNumber) && accountDetails[6].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deleteAccountDetails(String accountNumber) {
        try {
            File inputFile = new File(ACCOUNTS_FILE);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = accountNumber;
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] accountDetails = currentLine.split(",");

                if (!accountDetails[0].equals(lineToRemove)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }

            writer.close();
            reader.close();

            // Delete the original file and rename the temporary file
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                throw new IOException("Failed to delete the original file: " + ACCOUNTS_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateAccountHolderName(String accountNumber, String newAccountHolderName) {
        updateAccountDetails(accountNumber, 1, newAccountHolderName);
    }

    public void updateAge(String accountNumber, int newAge) {
        updateAccountDetails(accountNumber, 2, String.valueOf(newAge));
    }

    public void updatePhoneNumber(String accountNumber, String newPhoneNumber) {
        updateAccountDetails(accountNumber, 3, newPhoneNumber);
    }

    public void updateSex(String accountNumber, String newSex) {
        updateAccountDetails(accountNumber, 4, newSex);
    }

    public void updateAccountType(String accountNumber, String newAccountType) {
        updateAccountDetails(accountNumber, 5, newAccountType);
    }

    public void updatePassword(String accountNumber, String newPassword) {
        updateAccountDetails(accountNumber, 6, newPassword);
    }

    private void updateAccountDetails(String accountNumber, int fieldIndex, String newValue) {
        try {
            File inputFile = new File(ACCOUNTS_FILE);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] accountDetails = currentLine.split(",");

                if (accountDetails[0].equals(accountNumber)) {
                    accountDetails[fieldIndex] = newValue;
                }

                writer.write(String.join(",", accountDetails) + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deposit(String accountNumber, double amount) {
        LocalDateTime dateTime = LocalDateTime.now();
        String transaction = dateTime + " - Deposit: " + amount;
        appendTransaction(accountNumber, transaction);
        updatebalance(accountNumber, amount);
    }


    public boolean withdraw(String accountNumber, double amount) {
        double bal = getbalance(accountNumber);

        if (bal >= amount) {
            bal -= amount;  // Deduct the amount from the balance
            LocalDateTime dateTime = LocalDateTime.now();
            String transaction = dateTime + " - Withdrawal: " + amount;
            appendTransaction(accountNumber, transaction);
            updatebalance(accountNumber, -amount);
            return true;
        } else {
            return false;
        }
    }


    public boolean transfer(String senderAccountNumber, String recipientAccountNumber, double amount) {
        double senderBalance = getbalance(senderAccountNumber);
        double recipientBalance = getbalance(recipientAccountNumber);

        if (senderBalance >= amount) {
            LocalDateTime dateTime = LocalDateTime.now();
            String senderTransaction = dateTime + " - Transfer to " + recipientAccountNumber + ": " + amount;
            String recipientTransaction = dateTime + " - Transfer from " + senderAccountNumber + ": " + amount;

            appendTransaction(senderAccountNumber, senderTransaction);
            appendTransaction(recipientAccountNumber, recipientTransaction);
            updatebalance(senderAccountNumber, -amount);
            updatebalance(recipientAccountNumber, amount);
            return true;
        } else {
            return false;
        }
    }

    public void displayTransactionHistory(String accountNumber) {
        try (Scanner scanner = new Scanner(new File(TRANSACTION_FILE))) {
            System.out.println("Transaction History:");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] transactionDetails = line.split(",");

                if (transactionDetails[0].equals(accountNumber)) {
                    System.out.println(transactionDetails[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void displayAccountInformation(String accountNumber) {
        try (Scanner scanner = new Scanner(new File(ACCOUNTS_FILE))) {
            System.out.println("Account Information:");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] accountDetails = line.split(",");

                if (accountDetails[0].equals(accountNumber)) {
                    System.out.println("Account Number: " + accountDetails[0]);
                    System.out.println("Account Holder Name: " + accountDetails[1]);
                    System.out.println("Age: " + accountDetails[2]);
                    System.out.println("Phone Number: " + accountDetails[3]);
                    System.out.println("Sex: " + accountDetails[4]);
                    System.out.println("Account Type: " + accountDetails[5]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void displayBalance(String accountNumber) {
        double balance = getbalance(accountNumber);
        System.out.println("Account Balance: " + balance);
    }

    private double getbalance(String accountNumber) {
        double bal = 0.0;

        try (Scanner scanner = new Scanner(new File(ACCOUNTS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] accountDetails = line.split(",");

                if (accountDetails[0].equals(accountNumber)) {
                    bal = Double.parseDouble(accountDetails[7]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bal;
    }
    
    private void updatebalance(String accountNumber, double amount) {
        try {
            File inputFile = new File(ACCOUNTS_FILE);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] accountDetails = currentLine.split(",");

                if (accountDetails[0].equals(accountNumber)) {
                    double bal = Double.parseDouble(accountDetails[7]);
                    bal += amount;
                    accountDetails[7] = String.valueOf(bal);
                }

                writer.write(String.join(",", accountDetails) + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                throw new IOException("Failed to update the original file: " + ACCOUNTS_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendTransaction(String accountNumber, String transaction) {
        try (FileWriter fileWriter = new FileWriter(TRANSACTION_FILE, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(accountNumber + "," + transaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

