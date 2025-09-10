package jdbc_Project.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Student {
	 
	public static final String URL = "jdbc:postgresql://localhost:5432/StudentDB";
	public static final String username = "postgres";
	public static final String password = "root";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int choice;

		do {
			System.out.println("\n--- Student Management System ---");
			System.out.println("1. Add Student");
			System.out.println("2. View All Students");
			System.out.println("3. Update Student");
			System.out.println("4. Delete Student");
			System.out.println("5. Exit");
			System.out.print("Enter your choice: ");
			choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {

			case 1:
				addStudent(sc);
				break;

			case 2:
				viewAllStudents();
				break;

			case 3:
				updateStudent(sc);
				break;

			case 4:
				deleteStudent(sc);
				break;

			case 5:
				System.out.println("Exiting");
			}
		} while (choice != 5);

		sc.close();

	}

	private static void addStudent(Scanner sc) {
		System.out.println("Enter Student name : ");
		String name = sc.nextLine();
		System.out.println("Enter Age : ");
		int age = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter Course : ");
		String course = sc.nextLine();

		String sql = "INSERT INTO Student (name,age,course) VALUES (?,?,?)";
		try {
			Connection con = DriverManager.getConnection(URL, username, password);

			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, age);
			pst.setString(3, course);

			int rowsInserted = pst.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Student added successfully!");
			}
		} catch (SQLException e) {
			System.out.println("Error adding student: " + e.getMessage());
		}
	}

	private static void viewAllStudents() {
		String sql = "Select * from Student";

		try {
			Connection con = DriverManager.getConnection(URL, username, password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (!rs.isBeforeFirst()) {
				System.out.println("No students found.");
				return;
			}

			// Formatting output
			System.out.println("\nID | Name          | Age  | Course");
			System.out.println("-------------------------------------");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String course = rs.getString("course");
				System.out.printf("%-2d | %-12s | %-5d | %-16s%n", id, name, age, course);
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving Students: " + e.getMessage());
		}
	}

	private static void updateStudent(Scanner sc) {
		viewAllStudents(); // Show list first
		System.out.print("\nEnter the ID of the student to update: ");
		int id = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter the new Student: ");
		String newCourse = sc.nextLine();

		String sql = "UPDATE Student SET course = ? WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(URL, username, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, newCourse);
			pstmt.setInt(2, id);

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Student updated successfully!");
			} else {
				System.out.println("No Student found with ID: " + id);
			}
		} catch (SQLException e) {
			System.out.println("Error updating student: " + e.getMessage());
		}
	}

	private static void deleteStudent(Scanner scanner) {
		viewAllStudents(); // Show list first
		System.out.print("\nEnter the ID of the student to delete: ");
		int id = scanner.nextInt();

		String sql = "DELETE FROM Student WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(URL, username, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Student deleted successfully!");
			} else {
				System.out.println("No Student found with ID: " + id);
			}
		} catch (SQLException e) {
			System.out.println("Error deleting Student: " + e.getMessage());
		}
	}

}
