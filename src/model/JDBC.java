//class for testing purposes
package model;

import java.sql.*;


public class JDBC {
    public static void main(String[] args) {
        String URL = "jdbc:postgresql://localhost:5432/";
        String USER = "postgres";
        String PASS = "12345";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO utilisateur (id_utilisateur, nom, email, mot_de_passe, role, balance) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, 0);
                    ps.setString(2, "John Doe");
                    ps.setString(3, "johndoe123@gmail.com");
                    ps.setString(4, "password123");
                    ps.setString(5, "admin");
                    ps.setInt(6, 50);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}