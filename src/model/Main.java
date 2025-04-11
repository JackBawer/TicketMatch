package model;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        UtilisateurDAOImpl utilisateurDAO = new UtilisateurDAOImpl();

        try {
            // Register a new user
            Utilisateur newUser = new Utilisateur(0, "Jane Doe", "jane.doe@example.com", "securePassword123", "client");
            boolean isRegistered = utilisateurDAO.register(newUser);
            if (isRegistered) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("User registration failed.");
            }

            // Authenticate the registered user
            Utilisateur authenticatedUser = utilisateurDAO.authenticate("jane.doe@example.com", "securePassword123");
            if (authenticatedUser != null) {
                System.out.println("Authentication successful. Welcome, " + authenticatedUser.getNom() + "!");
                System.out.println("Role: " + authenticatedUser.getRole());
            } else {
                System.out.println("Authentication failed. Invalid email or password.");
            }

            // Test authentication with incorrect password
            Utilisateur failedAuthUser = utilisateurDAO.authenticate("jane.doe@example.com", "wrongPassword");
            if (failedAuthUser == null) {
                System.out.println("Authentication failed as expected with incorrect password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}