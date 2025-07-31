package com.furi.internetbanking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Scanner;

@Setter
@Getter
@Builder

public class Customer {
    private String firstName;
    private String lastName;
    private long identityNumber;
    private static final Scanner scanner = new Scanner(System.in);


    public String customerToString() {
        return firstName + ";" + lastName + ";" + identityNumber;
    }


    public String toString() {
        return String.format("%-20s %-20s %-20s", firstName, lastName, identityNumber);
    }

    public static long readIdentityNumber() {

        long identityNumber;

        while (true) {
            System.out.println("Please enter the Identity number ");
            while (!scanner.hasNextLong()) {
                System.out.println("Error! Please enter only digits.");
                scanner.next();
            }
            identityNumber = scanner.nextLong();
            scanner.nextLine();
            int signsNumber = String.valueOf(identityNumber).length();

            if (signsNumber == 11) {
                return identityNumber;
            } else {
                System.out.println("Error! The Identity number must have exactly 11 digits ");
            }
        }
    }

    public static String readLastName() {
        System.out.println("Please enter surname");
        return scanner.nextLine();
    }

    public static String readFirstName() {
        System.out.println("Please enter name");
        return scanner.nextLine();
    }

    public static Customer getCustomer() {
        String firstName = readFirstName();
        String lastName = readLastName();
        long identityNumber = readIdentityNumber();
        return new Customer(firstName, lastName, identityNumber);
    }

    public static void showCustomers( ArrayList<Customer> customersCollection ) {
        System.out.println("\u001B[33m" + "Customers list:  ");
        System.out.printf("\u001B[32m" + "   %-20s %-20s %-20s%n", "Name", "Surname", "Identity number" + "\u001B[0m");
        int idx = 1;
        for (Customer customer : customersCollection) {
            System.out.println(idx + ". " + customer.toString());
            idx++;
        }
    }

    public static Customer customerChoice( ArrayList<Customer> customersCollection ) {
        System.out.println("Enter the client number to display the balance.");
        int customerChoice;
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            scanner.next();

        }
        customerChoice = scanner.nextInt();
        Customer customerChosen = customersCollection.get(customerChoice - 1);
        System.out.println("Name: " + customerChosen.getFirstName());
        System.out.println("Surname: " + customerChosen.getLastName());
        System.out.println("Identity number: " + customerChosen.getIdentityNumber());

        return customerChosen;
    }

    public static void showCustomerBalance( ArrayList<Customer> customersCollection, ArrayList<CustomerAccount> customerAccountsCollection, ArrayList<Account> accountsCollection ) {
        long customerIdentityNumber = Customer.customerChoice(customersCollection).getIdentityNumber();
        for (CustomerAccount customerAccount : customerAccountsCollection) {
            long identityNumber = customerAccount.getCustomerAccountIndentity();
            if (customerIdentityNumber == identityNumber) {
                System.out.println("Account number  " + customerAccount.getCustomerAccountNumber());
                long customerAccountNumber = customerAccount.getCustomerAccountNumber();

                for (Account account : accountsCollection) {
                    long accountNumber = account.getAccountNumber();
                    if (customerAccountNumber == accountNumber) {
                        System.out.println("The balance is:  " + account.getBalance() + " zł ");
                    }
                }
            }
        }
    }
}


