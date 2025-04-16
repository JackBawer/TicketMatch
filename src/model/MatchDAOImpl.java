package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAOImpl implements MatchDAO {

    @Override
    public Match get(int id) throws SQLException {
        String sql = "select * from match where 'id' = ?";
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Match(
                            rs.getInt("id_match"),
                            rs.getString("equipe1"),
                            rs.getString("equipe2"),
                            rs.getTimestamp("date_match").toLocalDateTime().toLocalDate(),
                            rs.getTimestamp("heure_match").toLocalDateTime().toLocalTime(),
                            rs.getString("lieu"),
                            rs.getInt("capacite_stade")
                            );
                }
            }
        }
        return null;
    }

    @Override
    public List<Match> getAll() throws SQLException {
        List<Match> matches = new ArrayList<>();
        String sql = "select * from match";
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    matches.add(new Match(
                            rs.getInt("id_match"),
                            rs.getString("equipe1"),
                            rs.getString("equipe2"),
                            rs.getTimestamp("date_match").toLocalDateTime().toLocalDate(),
                            rs.getTimestamp("heure_match").toLocalDateTime().toLocalTime(),
                            rs.getString("lieu"),
                            rs.getInt("capacite_stade")
                    ));
                }
            }
        }
        return matches;
    }

    @Override
    public int save(Match match) throws SQLException {
        if (match.getIdMatch() > 0) {
            return update(match);
        } else {
            return insert(match);
        }
    }

    @Override
    public int insert(Match match) throws SQLException {
        String sql = "insert into match (equipe1, equipe2, date_match, heure_match, lieu, capacite_stade) values(?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, match.getTeam1());
            ps.setString(2, match.getTeam2());
            ps.setObject(3, match.getMatchDate());
            ps.setObject(4, match.getMatchTime());
            ps.setString(5, match.getLocation());
            ps.setInt(6, match.getStadiumCap());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int update(Match match) throws SQLException {
        String sql = "update match set 'equipe1' = ?, 'equipe2' = ?, 'date_match' = ?, 'heure_match' = ?, 'lieu' = ?, 'capacite_stade' = ? where id_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, match.getTeam1());
            ps.setString(2, match.getTeam2());
            ps.setObject(3, match.getMatchDate());
            ps.setObject(4, match.getMatchTime());
            ps.setString(5, match.getLocation());
            ps.setInt(6, match.getStadiumCap());

            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Match match) {
        String sql = "delete from match where id_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, match.getIdMatch());
                return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
