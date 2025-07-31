package com.furi.internetbanking;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Investment {

    private int accountNumber;
    private double investmentSum;
    private double investmentPercentage;
    private boolean active;
    private LocalDate dateOfInvestmentTaken;
    private int dateOfInvestmentEnd;
    private double interest;
    private int investmentDuration;
    private int dayOfMonthForPayment;


    public String investmentToString() {
        return accountNumber + ";" + investmentSum + ";" + investmentPercentage + ";" + active + ";" + dateOfInvestmentTaken + ";" + dateOfInvestmentEnd + ";" + interest + ";" + investmentDuration + ";" + dayOfMonthForPayment;
    }

    public static Investment createInvestment() {
        int accountNumber = readAccountNumber();
        double investmentSum = readInvestmentSum();
        double investmentPercentage = Math.round(Math.random() * 5 + 5);
        boolean active = true;
        LocalDate dateOfInvestmentTaken = LocalDate.now();
        int investmentDuration = readInvestmentDuration();
        int dateOfInvestmentEnd = investmentDuration + Integer.parseInt(String.valueOf(LocalDate.now()).substring(5, 7));
        double interest = (investmentSum * investmentPercentage) / 100;
        int dayOfMonthForPayment = 10;
        return new Investment(accountNumber, investmentSum, investmentPercentage, active, dateOfInvestmentTaken, dateOfInvestmentEnd, interest, investmentDuration, dayOfMonthForPayment);
    }

    public static void doInvestment( ArrayList<Account> accountsCollection, ArrayList<Investment> investmentCollection ) throws IOException {
        Investment investment = createInvestment();
        int decision = Investment.decisionForInvestment(investment.getInvestmentSum(), investment.getInvestmentPercentage(), investment.getInvestmentDuration());
        if (decision == 1) {
            List<String> transactionHistoryCollection = new ArrayList<>();
            transactionHistoryCollection.add("A deposit has been opened for the amount of : " + investment.getInvestmentSum() + " zł");
            Account.saveAccountTransactionToFile(transactionHistoryCollection, investment.getAccountNumber());
            Account account = Account.findAccount(accountsCollection, investment.getAccountNumber());
            double balance = account.getBalance();
            System.out.println("Old amount: " + balance);
            double newBalance = balance - investment.getInvestmentSum();
            account.setBalance(newBalance);
            System.out.println("New amount: " + newBalance);
            FileUtil.saveAccountsToFile(accountsCollection);
            investmentCollection.add(investment);
            FileUtil.saveInvestmentToFile(investmentCollection);
        }
    }

    public static void investmentBackToOwner( ArrayList<Investment> investmentCollection, ArrayList<Account> accountsCollection ) throws IOException {
       String date = String.valueOf(LocalDate.now());
        int actualMonth = Integer.parseInt(date.substring(5, 7));
        for (Investment investment : investmentCollection) {
            int endingMonth = investment.getDateOfInvestmentEnd();
            boolean active = investment.getActive();
            if (actualMonth == endingMonth && active) {
                int accountNumber = investment.getAccountNumber();
                Account account = Account.findAccount(accountsCollection, accountNumber);
                double balance = account.getBalance();
                double newBalance = balance + investment.getInvestmentSum() + investment.getInterest();
                account.setBalance(newBalance);
                investment.setActive(false);
                List<String> transactionHistoryCollection = new ArrayList<>();
                transactionHistoryCollection.add("The deposit has been terminated with the amount: " + investment.getInvestmentSum() + " The amount has been credited to the account: " + (investment.getInvestmentSum() + investment.getInterest()));
                Account.saveAccountTransactionToFile(transactionHistoryCollection, accountNumber);
            }
        }
        FileUtil.saveAccountsToFile(accountsCollection);
        FileUtil.saveInvestmentToFile(investmentCollection);
    }

    public boolean getActive() {
        return active;
    }

    public static int decisionForInvestment( double investmentSum, double investmentPercentage, int investmentDuration ) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to open a fixed-term deposit for the amount: " + investmentSum + ", interest rate " + investmentPercentage + "% for a term of " + investmentDuration + " months?");
        System.out.println("1.Yes ");
        System.out.println("2.No ");
        return scanner.nextInt();

    }

    public static int readInvestmentDuration() {
        System.out.println("Please specify for how many months you would like to open the fixed-term deposit ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static double readInvestmentSum() {
        System.out.println("Please specify the amount of the deposit ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();

    }

    public static int readAccountNumber() {
        System.out.println("Please provide the account number for which you want to open the deposit ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
