package com.furi.internetbanking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Scanner;

@Setter
@Getter
@Builder

public class CustomerAccount {
    private int customerAccountNumber;
    private long customerAccountIndentity;

    public String CustomerAccountToString() {
        return customerAccountNumber + ";" + customerAccountIndentity;
    }
    public static CustomerAccount createCustomerAccount( ArrayList<Customer> customersCollection, ArrayList<Account> accountsCollection ) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj indeks klienta");
        int customerIndex = scanner.nextInt();
        System.out.println("Podaj indeks konta ");
        int accountIndex = scanner.nextInt();
        Customer customerChoice = customersCollection.get(customerIndex);
        Account accountChoice = accountsCollection.get(accountIndex);
        int customerAccountNumber = accountChoice.getAccountNumber();
        long customerAccountIndentity = customerChoice.getIdentityNumber();
        System.out.println(customerChoice);
        System.out.println(accountChoice);

        return new CustomerAccount(customerAccountNumber, customerAccountIndentity);
    }

}
