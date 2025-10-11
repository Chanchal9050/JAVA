import java.sql.*;
import java.util.*;

public class StudentManagementApp {

    // ===== MODEL =====
    static class Student {
        private int studentID;
        private String name;
        private String department;
        private double marks;

        public Student(int studentID, String name, String department, double marks) {
            this.studentID = studentID;
            this.name = name;
            this.department = department;
            this.marks = marks;
        }

        public int getStudentID() { return studentID; }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getMarks() { return marks; }
    }

    // ===== CONTROLLER =====
    static class StudentDAO {
        private Connection con;

        public StudentDAO() throws Exception {
            // Step 1: Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Connect to Database (change db name, username, password)
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schooldb", "root", "yourpassword"
            );
        }

        // Add a new student
        public void addStudent(Student s) throws SQLException {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Student(StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)"
            );
            ps.setInt(1, s.getStudentID());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setDouble(4, s.getMarks());
            ps.executeUpdate();
            System.out.println("✅ Student Added Successfully!");
        }

        // View all students
        public void viewStudents() throws SQLException {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Student");

            System.out.println("\nStudentID\tName\tDepartment\tMarks");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("StudentID") + "\t\t" +
                    rs.getString("Name") + "\t" +
                    rs.getString("Department") + "\t\t" +
                    rs.getDouble("Marks")
                );
            }
        }

        // Update marks
        public void updateMarks(int id, double marks) throws SQLException {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Student SET Marks=? WHERE StudentID=?"
            );
            ps.setDouble(1, marks);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Marks Updated Successfully!");
            else
                System.out.println("⚠️ Student not found!");
        }

        // Delete student
        public void deleteStudent(int id) throws SQLException {
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM Student WHERE StudentID=?"
            );
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Student Deleted Successfully!");
            else
                System.out.println("⚠️ Student not found!");
        }
    }

    // ===== VIEW (MAIN METHOD) =====
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            StudentDAO dao = new StudentDAO();

            while (true) {
                System.out.println("\n===== Student Management System =====");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Marks");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student ID: ");
                        int id = sc.nextInt();
                        System.out.print("Enter Name: ");
                        String name = sc.next();
                        System.out.print("Enter Department: ");
                        String dept = sc.next();
                        System.out.print("Enter Marks: ");
                        double marks = sc.nextDouble();

                        dao.addStudent(new Student(id, name, dept, marks));
                    }
                    case 2 -> dao.viewStudents();
                    case 3 -> {
                        System.out.print("Enter Student ID to update: ");
                        int id = sc.nextInt();
                        System.out.print("Enter new Marks: ");
                        double marks = sc.nextDouble();
                        dao.updateMarks(id, marks);
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = sc.nextInt();
                        dao.deleteStudent(id);
                    }
                    case 5 -> {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid Choice! Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
