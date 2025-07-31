package com.furi.internetbanking;
import java.io.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtil {

    public static void saveCustomersToFile( ArrayList<Customer> customersCollection ) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("CustomersCollection.txt")) {
            System.out.println("The following list of clients has been saved:");
            for (Customer customer : customersCollection) {
                System.out.println(customer.customerToString());
                printWriter.println(customer.customerToString());
            }
        }
    }

    public static void loadCustomersFromFile( ArrayList<Customer> customersCollection ) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("CustomersCollection.txt"))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 3) {
                    System.out.println("Wrong record: " + line);
                    continue;
                }
                Customer customer = createCustomer(parts);
                customersCollection.add(customer);
            }
        } catch (IOException e) {
            System.out.println("The file could not be read ");
        }
    }

    public static void loadCustomersAccountsFromFile( ArrayList<CustomerAccount> customerAccountsCollection ) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("CustomersAccountsCollection.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 2) {
                    System.out.println("Wrong record: " + line);
                    continue;
                }
                CustomerAccount customerAccount = createCustomerAccount(parts);
                customerAccountsCollection.add(customerAccount);
            }
        } catch (IOException e) {
            System.out.println("The file could not be read ");
        }
    }

    private static CustomerAccount createCustomerAccount( String[] parts ) {
        int customerAccountNumber = Integer.parseInt(parts[0]);
        long customerAccountIdentity = Long.parseLong(parts[1]);
        return new CustomerAccount(customerAccountNumber, customerAccountIdentity);
    }

    public static void saveAccountsToFile( ArrayList<Account> accountsCollection ) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("AccountsCollection.txt")) {
            System.out.println("The data has been saved to the file ");
            for (Account account : accountsCollection) {
                printWriter.println(account.accountToString());
            }
        }

    }

    public static void saveInvestmentToFile( ArrayList<Investment> investmentsCollection ) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("InvestmentsCollection.txt")) {
            for (Investment investment : investmentsCollection) {
                printWriter.println(investment.investmentToString());
            }
        }

    }

    public static void loadInvestmentFromFile( ArrayList<Investment> investmentsCollection ) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("InvestmentsCollection.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 9) {
                    System.out.println("Wrong record: " + line);
                    continue;
                }
                Investment investment = createInvestment(parts);
                investmentsCollection.add(investment);
            }
        } catch (IOException e) {
            System.out.println("The file could not be read ");
        }
    }

    private static Investment createInvestment( String[] parts ) {

        int accountNumber = Integer.parseInt(parts[0]);
        double investmentSum = Double.parseDouble(parts[1]);
        double investmentPercentage = Double.parseDouble(parts[2]);
        boolean active = Boolean.parseBoolean(parts[3]);
        LocalDate dateOfInvestmentTaken = LocalDate.parse(parts[4]);
        int dateOfInvestmentEnd = Integer.parseInt(parts[5]);
        double interest = Double.parseDouble(parts[6]);
        int investmentDuration = Integer.parseInt(parts[7]);
        int dayOfMonthForPayment = Integer.parseInt(parts[8]);
        return new Investment(accountNumber, investmentSum, investmentPercentage, active, dateOfInvestmentTaken, dateOfInvestmentEnd, interest, investmentDuration, dayOfMonthForPayment);
    }

    public static void saveCustomersAccountsToFile( ArrayList<CustomerAccount> customerAccountsCollection ) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("CustomersAccountsCollection.txt")) {
            for (CustomerAccount customerAccount : customerAccountsCollection) {
                System.out.println(customerAccount.CustomerAccountToString());
                printWriter.println(customerAccount.CustomerAccountToString());
            }
        }
    }

    public static void loadAccountsFromFile( ArrayList<Account> accountsCollection ) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("AccountsCollection.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 2) {
                    System.out.println("Wrong record: " + line);
                    continue;
                }
                Account account = createAccount(parts);
                accountsCollection.add(account);
            }
        } catch (IOException e) {
            System.out.println("The file could not be read ");
        }
    }


    private static Account createAccount( String[] parts ) {
        int accountNumber = Integer.parseInt(parts[0]);
        double balance = Double.parseDouble(parts[1]);
        return new Account(accountNumber, balance);
    }

    public static void saveCreditToFile( ArrayList<Credit> creditsCollection ) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("CreditsCollection.txt")) {
            for (Credit credit : creditsCollection) {
                printWriter.println(credit.creditToString());
            }
        }


    }

    public static void loadCreditFromFile( ArrayList<Credit> creditsCollection ) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("CreditsCollection.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 8) {
                    System.out.println("Wrong record: " + line);
                    continue;
                }
                Credit credit = createCredit(parts);
                creditsCollection.add(credit);
            }
        } catch (IOException e) {
            System.out.println("The file could not be read ");
        }
    }

    private static Credit createCredit( String[] parts ) {
        int accountNumber = Integer.parseInt(parts[0]);
        double creditSum = Double.parseDouble(parts[1]);
        double creditPercentage = Double.parseDouble(parts[2]);
        int numberOfInstallments = Integer.parseInt(parts[3]);
        LocalDate dateOfCreditTaken = LocalDate.parse(parts[4]);
        int dayOfMonthForPayment = Integer.parseInt(parts[5]);
        boolean active = Boolean.parseBoolean(parts[6]);
        double amountToRepaid = Double.parseDouble(parts[7]);
        return new Credit(accountNumber, creditSum, creditPercentage, numberOfInstallments, dateOfCreditTaken, dayOfMonthForPayment, active, amountToRepaid);
    }

    private static Customer createCustomer( String[] parts ) {
        String firstName = parts[0];
        String lastName = parts[1];
        long identityNumber = Long.parseLong(parts[2]);
        return new Customer(firstName, lastName, identityNumber);
    }

    private static String removeDiacritics( String text ) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static void searchInCustomerFile() throws IOException {
        boolean found;
        Scanner scanner = new Scanner(System.in);
        do {
            found = false;

            System.out.println("Please enter the first name, last name, or identity number.");
            String searchTerm = removeDiacritics(scanner.nextLine().toLowerCase());
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("CustomersCollection.txt"))) {
                String line;
                int lineNumber = 1;
                System.out.println("The following customers have been found: ");
                System.out.printf("\u001B[32m" + "%-5s %-20s %-20s %-20s%n", "No.", "Name", "Surname", "Identity number" + "\u001B[0m");

                while ((line = bufferedReader.readLine()) != null) {
                    String normalizedLine = removeDiacritics(line.toLowerCase());
                    if (normalizedLine.contains(searchTerm)) {

                        found = true;
                        String[] parts = line.split(";");
                        if (parts.length < 3) {
                            continue;
                        }
                        System.out.println(lineNumber + "     " + createCustomer(parts));
                    }

                    lineNumber++;
                }
                if (!found) {
                    System.out.println("No such client was found. Please try again ");

                }

            }

        } while (!found);
    }
}