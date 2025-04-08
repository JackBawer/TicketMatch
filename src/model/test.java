package model;

import model.Utilisateur;
import model.UtilisateurDAOImpl;

import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        UtilisateurDAOImpl utilisateurDAO = new UtilisateurDAOImpl();

        try {
            // Insert a new user
            Utilisateur newUser = new Utilisateur(0, "John Doe", "john.doe@example.com", "password123", "client");
            int newUserId = utilisateurDAO.insert(newUser);
            System.out.println("Inserted User ID: " + newUserId);

            // Retrieve the user by ID
            Utilisateur retrievedUser = utilisateurDAO.get(newUserId);
            System.out.println("Retrieved User: " + retrievedUser);

            // Update the user's information
            retrievedUser.setNom("John Updated");
            utilisateurDAO.update(retrievedUser);
            System.out.println("Updated User: " + utilisateurDAO.get(newUserId));

            // Retrieve all users
            System.out.println("All Users: " + utilisateurDAO.getAll());

            // Delete the user
            utilisateurDAO.delete(retrievedUser);
            System.out.println("User deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}