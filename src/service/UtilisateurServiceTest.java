package service;

import model.Utilisateur;
import model.UtilisateurDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurServiceTest {
    private UtilisateurDAOImpl utilisateurDAO;
    private UtilisateurService utilisateurService;

    @BeforeEach
    void setUp() {
        utilisateurDAO = new UtilisateurDAOImpl(); // Use real DAO
        utilisateurService = new UtilisateurService(utilisateurDAO); // Inject the DAO
    }

    @Test
    void testRegisterUser_Success() throws SQLException {
        boolean result = utilisateurService.registerUser("John Doe", "john.doe@example.com", "password123", "client");

        assertTrue(result);

        Utilisateur registeredUser = utilisateurDAO.authenticate("john.doe@example.com", "password123");
        assertNotNull(registeredUser);
        assertEquals("John Doe", registeredUser.getNom());
    }

    @Test
    void testRegisterUser_DuplicateEmail() throws SQLException {
        utilisateurService.registerUser("John Doe", "john.doe@example.com", "password123", "client");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                utilisateurService.registerUser("John Doe", "john.doe@example.com", "password123", "client"));

        assertEquals("Email already exists: john.doe@example.com", exception.getMessage());
    }

    @Test
    void testRegisterUser_InvalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                utilisateurService.registerUser("John Doe", "invalid-email", "password123", "client"));

        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    void testAuthenticateUser_Success() throws SQLException {
        utilisateurService.registerUser("Jane Doe", "jane.doe@example.com", "password123", "client");

        Utilisateur result = utilisateurService.authenticateUser("jane.doe@example.com", "password123");

        assertNotNull(result);
        assertEquals("Jane Doe", result.getNom());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() throws SQLException {
        utilisateurService.registerUser("Jane Doe", "jane.doe@example.com", "password123", "client");

        Utilisateur result = utilisateurService.authenticateUser("jane.doe@example.com", "wrongpassword");

        assertNull(result);
    }
}