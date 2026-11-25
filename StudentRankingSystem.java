import java.io.*;
import java.util.Scanner;

class Student {
    String rollNo;
    String name;
    int marks;

    Student(String rollNo, String name, int marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
    }
}

public class StudentRankingSystem {
    static final int MAX_STUDENTS = 100;
    static final String FILE_NAME = "students.txt";
    static Student[] students = new Student[MAX_STUDENTS];
    static int count = 0;

    public static void main(String[] args) {
        loadFromFile();
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n--- Student Ranking System ---");
            System.out.println("1. Add Student");
            System.out.println("2. Display Rank List");
            System.out.println("3. Search (by Roll or Name)");
            System.out.println("4. Show Topper");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addStudent(sc);
                    break;
                case 2:
                    displayRankList();
                    break;
                case 3:
                    searchStudent(sc);
                    break;
                case 4:
                    showTopper();
                    break;
                case 5:
                    saveToFile();
                    System.out.println("Data saved to " + FILE_NAME + ". Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
    }

    // 1. Add Student with validation and auto-generated roll number
    static void addStudent(Scanner sc) {
        if (count >= MAX_STUDENTS) {
            System.out.println("Student limit reached.");
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Marks: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid marks. Marks must be a number.");
            sc.next();
            return;
        }
        int marks = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (marks < 0 || marks > 100) {
            System.out.println("Invalid marks. Marks must be between 0 and 100.");
            return;
        }

        // Generate roll number starting from R101
        String rollNo = "R" + (101 + count);
        students[count] = new Student(rollNo, name, marks);
        count++;

        System.out.println("Student added. Roll: " + rollNo + " | " + name + " | " + marks);
    }

    // Helper method: Manual Bubble Sort in descending order of marks
    static Student[] getSortedStudents() {
        Student[] sorted = new Student[count];
        for (int i = 0; i < count; i++) {
            sorted[i] = students[i];
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (sorted[j].marks < sorted[j + 1].marks) {
                    Student temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        return sorted;
    }

    // 2. Display Rank List
    static void displayRankList() {
        if (count == 0) {
            System.out.println("No student records found.");
            return;
        }

        Student[] sorted = getSortedStudents();
        for (int i = 0; i < count; i++) {
            System.out.println("Rank " + (i + 1) + ": " + sorted[i].rollNo + " | " + sorted[i].name + " | " + sorted[i].marks);
        }
    }

    // 3. Linear Search by Roll Number or Name
    static void searchStudent(Scanner sc) {
        if (count == 0) {
            System.out.println("No student records available to search.");
            return;
        }

        System.out.print("Enter Roll Number or Name to search: ");
        String query = sc.nextLine().trim();

        Student[] sorted = getSortedStudents();
        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (sorted[i].rollNo.equalsIgnoreCase(query) || sorted[i].name.equalsIgnoreCase(query)) {
                System.out.println("Found: " + sorted[i].rollNo + " | " + sorted[i].name + " | " + sorted[i].marks + " | Rank: " + (i + 1));
                found = true;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    // 4. Show Topper
    static void showTopper() {
        if (count == 0) {
            System.out.println("No student records found.");
            return;
        }

        Student[] sorted = getSortedStudents();
        int topMarks = sorted[0].marks;

        System.out.println("--- Topper(s) ---");
        for (int i = 0; i < count; i++) {
            if (sorted[i].marks == topMarks) {
                System.out.println("Rank 1: " + sorted[i].rollNo + " | " + sorted[i].name + " | " + sorted[i].marks);
            } else {
                break;
            }
        }
    }

    // Load data from students.txt on startup
    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null && count < MAX_STUDENTS) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String rollNo = parts[0].trim();
                    String name = parts[1].trim();
                    int marks = Integer.parseInt(parts[2].trim());
                    students[count++] = new Student(rollNo, name, marks);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // Save data to students.txt on exit
    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < count; i++) {
                pw.println(students[i].rollNo + "," + students[i].name + "," + students[i].marks);
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}