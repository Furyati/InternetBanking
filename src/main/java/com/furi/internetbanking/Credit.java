package com.furi.internetbanking;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Setter
@Getter
@Builder

public class Credit {
    private int accountNumber;
    private double creditSum;
    private double creditPercentage;
    private int numberOfInstallments;
    private LocalDate dateOfCreditTaken;
    private int dayOfMonthForPayment;
    private boolean active;
    private double amountToRepaid;
    private static final Scanner scanner = new Scanner(System.in);

    public String creditToString() {
        return accountNumber + ";" + creditSum + ";" + creditPercentage + ";" + numberOfInstallments + ";" + dateOfCreditTaken + ";" + dayOfMonthForPayment + ";" + active + ";" + amountToRepaid;
    }

    public static Credit createCredit() {
        int accountNumber = readAccountNumberForCredit();
        double creditSum = readCreditSum();
        double creditPercentage = Math.round(Math.random() * 5 + 5);
        int numberOfInstallments = readNumberOfInstallments();
        LocalDate dateOfCreditTaken = LocalDate.now();
        int dayOfMonthForPayment = 10;
        boolean active = true;
        double amountToRepaid = creditSum + ((creditSum * creditPercentage) / 100);

        return new Credit(accountNumber, creditSum, creditPercentage, numberOfInstallments, dateOfCreditTaken, dayOfMonthForPayment, active, amountToRepaid);
    }

    public static int readAccountNumberForCredit() {
        System.out.println("Please enter the account number for which you would like to apply for a loan ");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid integer account number!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static double readCreditSum() {
        System.out.println("Please specify the amount of the loan ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number!");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    public static int readNumberOfInstallments() {
        System.out.println("Please specify the number of installments you would like to choose for the loan repayment ");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid integer number for installments!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static double generateCreditPercentage() {
        return Math.round((Math.random() * 5 + 5) * 100) / 100.0;
    }


    public static int decisionForCredit(double creditSum, double creditPercentage) {
        System.out.println("Are you sure you want to apply for a loan in the amount of: " + creditSum + " and the interest rate: " + creditPercentage + " % ?");
        System.out.println("1.Yes ");
        System.out.println("2.No ");

        int decision;
        while (true) {
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter 1 or 2!");
                scanner.next();
            }
            decision = scanner.nextInt();
            if (decision == 1 || decision == 2) {
                break;
            }
            System.out.println("Please enter 1 or 2!");
        }
        if (decision == 2) {
            System.out.println("Credit application cancelled by user.");
        }
        return decision;
    }


    public static void doCredit( ArrayList<Account> accountsCollection, ArrayList<Credit> creditsCollection ) throws IOException {
        Credit credit = Credit.createCredit();
        int decision = Credit.decisionForCredit(credit.getCreditSum(), credit.getCreditPercentage());
        if (decision == 1) {
            List<String> transactionHistoryCollection = new ArrayList<>();
            transactionHistoryCollection.add("A loan has been issued for the amount of: " + credit.getCreditSum() + " zł");
            Account.saveAccountTransactionToFile(transactionHistoryCollection, credit.getAccountNumber());
            Account account = Account.findAccount(accountsCollection, credit.getAccountNumber());
            double balance = account.getBalance();


            System.out.println("Old amount: " + balance);
            double newBalance = balance + credit.getCreditSum();
            account.setBalance(newBalance);
            System.out.println("New Amount: " + newBalance);
            FileUtil.saveAccountsToFile(accountsCollection);
            creditsCollection.add(credit);
            FileUtil.saveCreditToFile(creditsCollection);


        }


    }

    public static void doCreditInstallment( ArrayList<Credit> creditsCollection, ArrayList<Account> accountsCollection ) throws IOException {
        String date = String.valueOf(LocalDate.now());
        int day = Integer.parseInt(date.substring(8, 10));
        if (day == 10) {
            for (Credit credit : creditsCollection) {
                int installmentNumber = credit.getNumberOfInstallments();
                int accountNumber = credit.getAccountNumber();
                double creditPercentage = credit.getCreditPercentage();
                double creditSum = credit.getCreditSum();
                if (installmentNumber <= 0) {
                    System.out.println("Invalid number of installments: " + installmentNumber);
                    continue;
                }
                double installmentAmount = (creditSum + (creditSum * (creditPercentage / 100))) / installmentNumber;
                boolean active = credit.isActive();
                if (active) {
                    Account account = Account.findAccount(accountsCollection, accountNumber);
                    double balance = account.getBalance();
                    double newBalance = balance - installmentAmount;
                    account.setBalance(newBalance);
                    double amountToRepaid = credit.getAmountToRepaid();
                    double newAmountToRepaid = amountToRepaid - installmentAmount;
                    credit.setAmountToRepaid(newAmountToRepaid);
                    List<String> transactionHistoryCollection = new ArrayList<>();
                    transactionHistoryCollection.add("The loan installment has been collected in the amount of: " + installmentAmount + " zł");
                    Account.saveAccountTransactionToFile(transactionHistoryCollection, accountNumber);
                    if (newAmountToRepaid <= 0) {
                        credit.setActive(false);

                    }
                }
            }
            FileUtil.saveAccountsToFile(accountsCollection);
            FileUtil.saveCreditToFile(creditsCollection);
        }
    }
}

