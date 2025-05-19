package model;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        UtilisateurDAOImpl utilisateurDAO = new UtilisateurDAOImpl();

        AdminPanel adminPanel = new AdminPanel();
        UserPanel userPanel = new UserPanel();

        try {
            // Register a new user
            Utilisateur newUser = new Utilisateur(1, "John Doe", "johndoe123@example.com", "password123", "admin", 50);
            boolean isRegistered = utilisateurDAO.register(newUser);
            if (isRegistered) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("User registration failed.");
            }

            // Authenticate the registered user
            Utilisateur authenticatedUser = utilisateurDAO.authenticate("johndoe123@gmail.com", "password123");
            if (authenticatedUser != null) {
                System.out.println("Authentication successful. Welcome, " + authenticatedUser.getNom() + "!");
                System.out.println("Role: " + authenticatedUser.getRole());

                // Check if user is admin and grant privileges accordingly
                if (authenticatedUser.getRole().equals("admin")) {
                    System.out.println("Admin privileges granted.");
                    adminPanel.initiate();
                } else {
                    userPanel.initiate(newUser.getIdUtilisateur());
                }

            } else {
                System.out.println("Authentication failed. Invalid email or password.");
            }

            // Test authentication with incorrect password
            Utilisateur failedAuthUser = utilisateurDAO.authenticate("johndoe123@gmail.com", "wrongPassword");
            if (failedAuthUser == null) {
                System.out.println("Authentication failed as expected with incorrect password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}