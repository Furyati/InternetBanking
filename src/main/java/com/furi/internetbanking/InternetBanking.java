package com.furi.internetbanking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InternetBanking {

    public static void main( String[] args ) throws IOException {
        ArrayList<Customer> customersCollection = new ArrayList<>();
        ArrayList<Account> accountsCollection = new ArrayList<>();
        ArrayList<CustomerAccount> customerAccountsCollection = new ArrayList<>();
        ArrayList<Credit> creditsCollection = new ArrayList<>();
        ArrayList<Investment> investmentsCollection = new ArrayList<>();
        try {
            FileUtil.loadCustomersFromFile(customersCollection);
            FileUtil.loadAccountsFromFile(accountsCollection);
            FileUtil.loadCustomersAccountsFromFile(customerAccountsCollection);
            FileUtil.loadCreditFromFile(creditsCollection);
            FileUtil.loadInvestmentFromFile(investmentsCollection);
            Credit.doCreditInstallment(creditsCollection, accountsCollection);
            Investment.investmentBackToOwner(investmentsCollection, accountsCollection);
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        while (userChoice != 9) {
            userChoice = getUserChoice(scanner);
            if (userChoice == 1) {
                Customer customer = Customer.getCustomer();
                customersCollection.add(customer);
                FileUtil.saveCustomersToFile(customersCollection);
            } else if (userChoice == 2) {
                Account account = Account.getAccount();
                accountsCollection.add(account);
                FileUtil.saveAccountsToFile(accountsCollection);
            } else if (userChoice == 3) {
                Customer.showCustomers(customersCollection);
                Account.showAccounts(accountsCollection);
                System.out.println("Choose which account to connect with which client. ");
                CustomerAccount customerAccount = CustomerAccount.createCustomerAccount(customersCollection, accountsCollection);
                customerAccountsCollection.add(customerAccount);
                FileUtil.saveCustomersAccountsToFile(customerAccountsCollection);
            } else if (userChoice == 4) {
                FileUtil.searchInCustomerFile();
                Customer.showCustomerBalance(customersCollection, customerAccountsCollection, accountsCollection);
            } else if (userChoice == 5) {
                Account.transaction(accountsCollection);
            } else if (userChoice == 6) {
                Account.showTransactionHistory();
            } else if (userChoice == 7) {
                Credit.doCredit(accountsCollection, creditsCollection);

            } else if (userChoice == 8) {
                Investment.doInvestment(accountsCollection, investmentsCollection);

            } else if (userChoice == 9) {
                System.out.println("Closing application...");
            }
        }
    }


    private static int getUserChoice( Scanner scanner ) {
        System.out.println("╔════════════════════════════════╗");
        System.out.println("║  1. Add customer               ║");
        System.out.println("║  2. Add account                ║");
        System.out.println("║  3. Add customer account       ║");
        System.out.println("║  4. Check balance              ║");
        System.out.println("║  5. Make internal transfer     ║");
        System.out.println("║  6. Show transaction history   ║");
        System.out.println("║  7. Take credit                ║");
        System.out.println("║  8. Take deposit               ║");
        System.out.println("║  9. Exit                       ║");
        System.out.println("╚════════════════════════════════╝");

        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number (1-9):");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > 9) {
            System.out.println("Please enter a valid number between 1 and 9.");
            return getUserChoice(scanner);
        }
        return choice;
    }

}
