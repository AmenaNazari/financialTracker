
package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }



    private static void loadTransactions(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue;

                try {
                    LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    transactions.add(transaction);
                } catch (Exception e) {
                    // Skip invalid line
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());
        }
    }





    private static void addDeposit(Scanner scanner) {

        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);


        System.out.print("Enter time (HH:mm:ss): ");
        String timeInput = scanner.nextLine();
        LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (amount <= 0) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(transaction);
        saveTransaction(transaction);

        System.out.println("Deposit added successfully!");
    }

    private static void saveTransaction(Transaction transaction) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(transaction.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }





    private static void addPayment(Scanner scanner) {
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);

        System.out.print("Enter time (HH:mm:ss): ");
        String timeInput = scanner.nextLine();
        LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if (amount <= 0) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        amount = -amount; //  Convert to negative since it's a payment

        Transaction transaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(transaction);
        saveTransaction(transaction);

        System.out.println("Payment added successfully!");
    }




    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.println("All Transactions:");
        System.out.println("Date       | Time     | Description            | Vendor                | Amount");
        for (Transaction tree : transactions) {
            System.out.printf("%-10s | %-8s | %-22s | %-20s | %10.2f\n",
                    tree.getDate(), tree.getTime(), tree.getDescription(), tree.getVendor(), tree.getAmount());
        }
    }



    private static void displayDeposits() {
        System.out.println("Deposits:");
        System.out.println("Date       | Time     | Description            | Vendor                | Amount");
        for (Transaction tree : transactions) {
            if (tree.getAmount() > 0) {
                System.out.printf("%-10s | %-8s | %-22s | %-20s | %10.2f\n",
                        tree.getDate(), tree.getTime(), tree.getDescription(), tree.getVendor(), tree.getAmount());
            }
        }
    }



    private static void displayPayments() {
        System.out.println("Payments:");
        System.out.println("Date       | Time     | Description            | Vendor                | Amount");
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                System.out.printf("%-10s | %-8s | %-22s | %-20s | %10.2f\n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("\n Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    reportMonthToDate();
                    break;

                case "2":
                    reportPreviousMonth();
                    break;

                case "3":
                    reportYearToDate();
                    break;


                case "4":
                    reportPreviousYear();
                    break;

                case "5":
                    searchByVendor(scanner);
                    break;


                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void reportMonthToDate() {
        System.out.println("=== Month To Date Transactions ===");
        LocalDate today = LocalDate.now();
        boolean found = false;

        for (Transaction tree : transactions) {
            LocalDate txDate = tree.getDate();
            if (txDate.getYear() == today.getYear() && txDate.getMonth() == today.getMonth()) {
                printTransaction(tree);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for this month.");
        }
    }

    private static void reportPreviousMonth() {
        System.out.println("\n=== Previous Month Transactions ===");
        LocalDate today = LocalDate.now();
        LocalDate firstDayThisMonth = today.withDayOfMonth(1);
        LocalDate firstDayLastMonth = firstDayThisMonth.minusMonths(1);
        boolean found = false;

        for (Transaction tree : transactions) {
            LocalDate txDate = tree.getDate();
            if (txDate.getYear() == firstDayLastMonth.getYear() && txDate.getMonth() == firstDayLastMonth.getMonth()) {
                printTransaction(tree);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for the previous month.");
        }
    }


    private static void reportYearToDate() {
        System.out.println("\n=== Year To Date Transactions ===");
        int currentYear = LocalDate.now().getYear();
        boolean found = false;

        for (Transaction tree : transactions) {
            LocalDate txDate = tree.getDate();
            if (txDate.getYear() == currentYear) {
                printTransaction(tree);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for the current year.");
        }
    }


    private static void reportPreviousYear() {
        System.out.println("\n=== Previous Year Transactions ===");
        int previousYear = LocalDate.now().getYear() - 1;
        boolean found = false;

        for (Transaction tree : transactions) {
            LocalDate txDate = tree.getDate();
            if (txDate.getYear() == previousYear) {
                printTransaction(tree);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for the previous year.");
        }
    }


    private static void searchByVendor(Scanner scanner) {
        System.out.print("Enter vendor name to search: ");
        String vendorInput = scanner.nextLine().trim().toLowerCase();

        boolean found = false;

        for (Transaction tree : transactions) {
            if (tree.getVendor().equalsIgnoreCase(vendorInput)) {
                printTransaction(tree);
                found = true;
            }
        }


        if (!found) {
            System.out.println("No transactions found for vendor: " + vendorInput);
        }
    }


    private static void printTransaction(Transaction tree) {
        System.out.printf("%-10s | %-8s | %-22s | %-20s | %10.2f\n",
                tree.getDate(), tree.getTime(), tree.getDescription(), tree.getVendor(), tree.getAmount());
    }
}











