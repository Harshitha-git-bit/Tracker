package com.example.task.Alephys;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

class Transaction {
    String type; // Income or Expense
    LocalDate date;
    String category;
    double amount;
    String description;

    public Transaction(String type, LocalDate date, String category, double amount, String description) {
        this.type = type;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return type + "," + date + "," + category + "," + amount + "," + description;
    }
}

public class ExpenseTracker {
    private static List<Transaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n==== Expense Tracker ====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load Transactions from File");
            System.out.println("5. Save Transactions to File");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 : addTransaction("INCOME");
                break;
                case 2 :  addTransaction("EXPENSE");
                break;
                case 3 : showMonthlySummary();
                break;
                case 4 : loadFromFile();
                break;
                case 5 : saveToFile();
                break;
                case 6 : running = false;
                break;
                default : System.out.println("Invalid choice!");
            }
        }
    }

    private static void addTransaction(String type) {
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter category (" + (type.equals("INCOME") ? "Salary, Business" : "Food, Rent, Travel") + "): ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        transactions.add(new Transaction(type, date, category, amount, description));
        System.out.println("Transaction added!");
    }

    private static void showMonthlySummary() {
        Map<Month, Double> incomeByMonth = new HashMap<>();
        Map<Month, Double> expenseByMonth = new HashMap<>();
        Map<String,Double> incomebycategory = new HashMap<>();
        Map<String,Double> expensebycategory = new HashMap<>();
        

        for (Transaction t : transactions) {
            Month month = t.date.getMonth();
            if (t.type.equals("INCOME")) {
                incomeByMonth.put(month, incomeByMonth.getOrDefault(month, 0.0) + t.amount);
            } else {
                expenseByMonth.put(month, expenseByMonth.getOrDefault(month, 0.0) + t.amount);
            }
            
            String category = t.category;
            if(category.equalsIgnoreCase("salary")||category.equalsIgnoreCase("business")) {
            	incomebycategory.put(category,incomebycategory.getOrDefault(category, 0.0) + t.amount);
            }
            else {
            	expensebycategory.put(category, expensebycategory.getOrDefault(category, 0.0) + t.amount);
            }
        }

        System.out.println("\n--- Monthly Summary ---");
        for (Month month : Month.values()) {
            double income = incomeByMonth.getOrDefault(month, 0.0);
            double expense = expenseByMonth.getOrDefault(month, 0.0);
            double balance = income - expense;

            if (income != 0.0 || expense != 0.0) {
                System.out.printf("%s - Income: %.2f, Expense: %.2f, Balance: %.2f%n", month, income, expense, balance);
            }
        }
        
        System.out.println("\n--- Summary By Category ---");
        for (Map.Entry<String, Double> entry : incomebycategory.entrySet()) {
            System.out.println("Category: " + entry.getKey() + ", Amount: " + entry.getValue());
        }
        for (Map.Entry<String, Double> entry : expensebycategory.entrySet()) {
            System.out.println("Category: " + entry.getKey() + ", Amount: " + entry.getValue());
        }
        
    }

    private static void loadFromFile() {
        System.out.print("Enter file name to load from: ");
        String filename = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length == 5) {
                    Transaction t = new Transaction(
                            parts[0],
                            LocalDate.parse(parts[1]),
                            parts[2],
                            Double.parseDouble(parts[3]),
                            parts[4]
                    );
                    transactions.add(t);
                    count++;
                }
            }
            System.out.println("Loaded " + count + " transactions from file.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        System.out.print("Enter file name to save to: ");
        String filename = scanner.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Transactions saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
