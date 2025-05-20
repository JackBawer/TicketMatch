package service;

import model.Utilisateur;
import model.UtilisateurDAOImpl;

import java.sql.SQLException;

public class UtilisateurService {
    private final UtilisateurDAOImpl utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAOImpl();
    }

    public UtilisateurService(UtilisateurDAOImpl utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    public boolean registerUser(String name, String email, String password, Utilisateur.userRole role) {
        if (name == null || email == null || password == null || role == null) {
            throw new IllegalArgumentException("All fields are required.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        Utilisateur newUser = new Utilisateur(0, name, email, password, role, 0);
        try {
            return utilisateurDAO.register(newUser);
        } catch (IllegalArgumentException e) {
            // Handle duplicate email gracefully
            System.err.println("Registration error: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Utilisateur authenticateUser(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required.");
        }
        try {
            return utilisateurDAO.authenticate(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}