package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public Utilisateur get(int id) throws SQLException {
        String sql = "SELECT id_utilisateur, nom, email, mot_de_passe, role FROM utilisateur WHERE id_utilisateur = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("mot_de_passe"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Utilisateur> getAll() throws SQLException {
        String sql = "SELECT id_utilisateur, nom, email, mot_de_passe, role FROM utilisateur";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role")
                );
                utilisateurs.add(utilisateur);
            }
        }
        return utilisateurs;
    }

    @Override
    public int save(Utilisateur utilisateur) throws SQLException {
        if (utilisateur.getIdUtilisateur() > 0) {
            return update(utilisateur);
        } else {
            return insert(utilisateur);
        }
    }

    @Override
    public int insert(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getMotDePasse());
            ps.setString(4, utilisateur.getRole());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int update(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id_utilisateur = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getMotDePasse());
            ps.setString(4, utilisateur.getRole());
            ps.setInt(5, utilisateur.getIdUtilisateur());

            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Utilisateur utilisateur) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, utilisateur.getIdUtilisateur());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Registration
    public boolean register(Utilisateur utilisateur) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        String insertQuery = "INSERT INTO utilisateur (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            // Check if the email already exists
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, utilisateur.getEmail());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new IllegalArgumentException("Email already exists: " + utilisateur.getEmail());
                }
            }

            // Insert the new user
            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                insertStmt.setString(1, utilisateur.getNom());
                insertStmt.setString(2, utilisateur.getEmail());
                insertStmt.setString(3, utilisateur.getMotDePasse());
                insertStmt.setString(4, utilisateur.getRole());
                return insertStmt.executeUpdate() > 0;
            }
        }
    }

    // Authentification
    public Utilisateur authenticate(String email, String password) throws SQLException {
        String sql = "SELECT id_utilisateur, nom, email, mot_de_passe, role FROM utilisateur WHERE email = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtils.verifyPassword(password, rs.getString("mot_de_passe"))) {
                    return new Utilisateur(
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            null, // Do not return the password
                            rs.getString("role")
                    );
                }
            }
        }
        return null; // Authentication failed
    }
}