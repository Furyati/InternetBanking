package com.furi.internetbanking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Setter
@Getter
@Builder

public class Account {
    private int accountNumber;
    private double balance;
    private static final Scanner scanner = new Scanner(System.in);


    public static void saveAccountTransactionToFile( List<String> transactionHistory, int accountNumber ) throws IOException {

        try (FileWriter fileWriter = new FileWriter("historyof" + accountNumber + ".txt", true)) {
            System.out.println("The following transactions have been saved ");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);

                fileWriter.write(transaction);
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.flush();
        }
    }


    public static void showTransactionHistory() {
        System.out.println("Please provide the account number for which you want to display the history ");
        int chosenSourceAccountNumber = scanner.nextInt();
        scanner.nextLine();
        File file = new File("historyof" + chosenSourceAccountNumber + ".txt");
        if (!file.exists()) {
            System.out.println("No transaction history found for account number: " + chosenSourceAccountNumber);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading history " + e.getMessage());
        }

    }


    public String accountToString() {
        return accountNumber + ";" + balance;
    }


    public String toString() {
        return String.format("%-20s %-20s", accountNumber, balance);
    }

    public static void showAccounts( ArrayList<Account> accountsCollection ) {
        System.out.println("\u001B[33m" + "Account list:  ");
        System.out.printf("\u001B[32m" + "   %-20s %-20s%n", "Account number ", "balance" + "\u001B[0m");
        int idx = 1;
        for (Account account : accountsCollection) {
            System.out.println(idx + ". " + account);
            idx++;
        }
    }

    public static int readAccountNumber() {
        System.out.println("Please provide the account number ");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid account number!");
            scanner.next();
        }
        int acc = scanner.nextInt();
        scanner.nextLine();
        return acc;
    }

    public static double readBalance() {
        System.out.println("Please provide the balance ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number for balance!");
            scanner.next();
        }
        double balance = scanner.nextDouble();
        scanner.nextLine();
        return balance;
    }

    public static Account getAccount() {
        int accountNumber = readAccountNumber();
        double balance = readBalance();
        return new Account(accountNumber, balance);
    }

    public static Account findAccount( ArrayList<Account> accountsCollection, int accountNumber ) {
        for (Account account : accountsCollection) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        System.out.println("The provided account number was not found: " + accountNumber);
        return null;
    }

    public static void transaction( ArrayList<Account> accountsCollection ) throws IOException {
        boolean transactionCompleted = false;
        int transferAmount;
        int chosenSourceAccountNumber;
        int chosenTargetAccountNumber;
        List<String> transactionHistoryCollection = new ArrayList<>();
        do {
            System.out.println("Please enter the transfer amount ");
            transferAmount = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Please provide the account number from which you want to make the transfer ");
            chosenSourceAccountNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Please provide the account number to which you want to make the transfer ");
            chosenTargetAccountNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.println("You are making a transfer in the amount of: " + transferAmount + " zł, from the account with the number:" + chosenSourceAccountNumber + " to the account with the number: " + chosenTargetAccountNumber);
            double accountSourceBalance;
            double accountTargetBalance;
            boolean sourceAccountFound = false;
            boolean targetAccountFound = false;
            Account sourceAccount = null;
            Account targetAccount = null;

            for (Account account : accountsCollection) {
                int accountNumber = account.getAccountNumber();

                if (chosenSourceAccountNumber == accountNumber) {
                    sourceAccountFound = true;
                    sourceAccount = account;
                }


                if (chosenTargetAccountNumber == accountNumber) {
                    targetAccountFound = true;
                    targetAccount = account;
                }


            }
            if (!sourceAccountFound) {
                System.out.println("Source account not found ");
            }
            if (!targetAccountFound) {
                System.out.println("Destination account not found ");
            }

            if (sourceAccountFound && targetAccountFound) {
                transactionCompleted = true;
                System.out.println("\u001B[32m" + "The transfer was completed successfully! " + "\u001B[0m");
                accountSourceBalance = sourceAccount.getBalance();
                double newAccountSourceBalance = accountSourceBalance - transferAmount;
                sourceAccount.setBalance(newAccountSourceBalance);
                accountTargetBalance = targetAccount.getBalance();
                double newAccountTargetBalance = accountTargetBalance + transferAmount;
                targetAccount.setBalance(newAccountTargetBalance);

            } else {
                System.out.println("Please try again ");
            }
        }
        while (!transactionCompleted);
        transactionHistoryCollection.add("A transfer has been made in the amount of: " + transferAmount + " zł, from account number: " + chosenSourceAccountNumber + " to account number: " + chosenTargetAccountNumber);
        saveAccountTransactionToFile(transactionHistoryCollection, chosenSourceAccountNumber);
        saveAccountTransactionToFile(transactionHistoryCollection, chosenTargetAccountNumber);
    }
}