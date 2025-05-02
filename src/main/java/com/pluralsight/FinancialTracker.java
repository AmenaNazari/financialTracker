
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


    // This method should load transactions from a file with the given file name.
    // If the file does not exist, it should be created.
    // The transactions should be stored in the `transactions` ArrayList.
    // Each line of the file represents a single transaction in the following format:
    // <date>|<time>|<description>|<vendor>|<amount>
    // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
    // After reading all the transactions, the file should be closed.
    // If any errors occur, an appropriate error message should be displayed.


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


// This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
    // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
    // The amount should be a positive number.
    // After validating the input, a new `Transaction` object should be created with the entered values.
    // The new deposit should be added to the `transactions` ArrayList.


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

    // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
    // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
    // The amount received should be a positive number then transformed to a negative number.
    // After validating the input, a new `Transaction` object should be created with the entered values.
    // The new payment should be added to the `transactions` ArrayList.


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
    // This method should display a table of all transactions in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.


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
    // This method should display a table of all deposits in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.


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
    // This method should display a table of all payments in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.

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
                // Genrate a report for all transactions within the current month,
                // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    reportPreviousMonth();
                    break;
                // Generate a report for all transactions within the previous month,
                // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    reportYearToDate();
                    break;
                // Generate a report for all transactions within the current year,
                // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    reportPreviousYear();
                    break;
                // Generate a report for all transactions within the previous year,
                // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    searchByVendor(scanner);
                    break;

                // Prompt the user to enter a vendor name, then generate a report for all transactions
                // with that vendor, including the date, time, description, vendor, and amount for each transaction.
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






    // This method filters the transactions by date and prints a report to the console.
    // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
    // The method loops through the transactions list and checks each transaction's date against the date range.
    // Transactions that fall within the date range are printed to the console.
    // If no transactions fall within the date range, the method prints a message indicating that there are no results.


// This method filters the transactions by vendor and prints a report to the console.
// It takes one parameter: vendor, which represents the name of the vendor to filter by.
// The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
// Transactions with a matching vendor name are printed to the console.
// If no transactions match the specified vendor name, the method prints a message indicating that there are no results.





