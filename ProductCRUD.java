import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static String url = "jdbc:mysql://localhost:3306/shopdb";
    static String user = "root";
    static String password = "yourpassword";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            con.setAutoCommit(false); // transaction handling
            while (true) {
                System.out.println("\n=== Product Management System ===");
                System.out.println("1. Insert Product");
                System.out.println("2. Display Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> insertProduct(con, sc);
                    case 2 -> readProducts(con);
                    case 3 -> updateProduct(con, sc);
                    case 4 -> deleteProduct(con, sc);
                    case 5 -> { con.close(); return; }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insertProduct(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Product Name: ");
            String name = sc.next();
            System.out.print("Enter Price: ");
            double price = sc.nextDouble();
            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Product(ProductName, Price, Quantity) VALUES (?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, qty);
            ps.executeUpdate();
            con.commit();
            System.out.println("✅ Product inserted successfully!");
        } catch (Exception e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    static void readProducts(Connection con) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Product");
        System.out.println("\nProductID\tName\tPrice\tQuantity");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + "\t\t" + rs.getString(2) + "\t" + rs.getDouble(3) + "\t" + rs.getInt(4));
        }
    }

    static void updateProduct(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = sc.nextInt();
            System.out.print("Enter new Price: ");
            double price = sc.nextDouble();
            System.out.print("Enter new Quantity: ");
            int qty = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE Product SET Price=?, Quantity=? WHERE ProductID=?"
            );
            ps.setDouble(1, price);
            ps.setInt(2, qty);
            ps.setInt(3, id);
            ps.executeUpdate();
            con.commit();
            System.out.println("✅ Product updated successfully!");
        } catch (Exception e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    static void deleteProduct(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = sc.nextInt();

            PreparedStatement ps = con.prepareStatement("DELETE FROM Product WHERE ProductID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            con.commit();
            System.out.println("✅ Product deleted successfully!");
        } catch (Exception e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }
}
