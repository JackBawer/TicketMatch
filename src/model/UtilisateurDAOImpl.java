package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public Utilisateur get(int id) throws SQLException {
        String sql = "SELECT id_utilisateur, nom, email, mot_de_passe, role, balance FROM utilisateur WHERE id_utilisateur = ?";
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
                            Utilisateur.userRole.valueOf(rs.getString("role")),
                            rs.getInt("balance")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Utilisateur> getAll() throws SQLException {
        String sql = "SELECT id_utilisateur, nom, email, mot_de_passe, role, balance FROM utilisateur";
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
                        Utilisateur.userRole.valueOf(rs.getString("role")),
                        rs.getInt("balance")
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
            ps.setString(4, utilisateur.getRole().name());

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
        String sql = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ?, balance = ? WHERE id_utilisateur = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getMotDePasse());
            ps.setString(4, utilisateur.getRole().name());
            ps.setDouble(5, utilisateur.getBalance());
            ps.setInt(6, utilisateur.getIdUtilisateur());

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
        String insertQuery = "INSERT INTO utilisateur (nom, email, mot_de_passe, role, balance) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            // Insert the new user
            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                insertStmt.setString(1, utilisateur.getNom());
                insertStmt.setString(2, utilisateur.getEmail());
                insertStmt.setString(3, utilisateur.getMotDePasse());
                insertStmt.setString(4, utilisateur.getRole().name());
                insertStmt.setDouble(5, utilisateur.getBalance());
                return insertStmt.executeUpdate() > 0;
            }
        }
    }

    // Authentification
    public Utilisateur authenticate(String email, String password) throws SQLException {
        String sql = "SELECT id_utilisateur, nom, email, mot_de_passe, role, balance FROM utilisateur WHERE email = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { //password hashing isn't working as intended
                    return new Utilisateur(
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("mot_de_passe"), // Do not return the password
                            Utilisateur.userRole.valueOf(rs.getString("role")),
                            rs.getInt("balance")
                    );
                }
            }
        }
        return null; // Authentication failed
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM utilisateur WHERE nom = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM utilisateur WHERE email = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}