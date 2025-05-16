package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TicketDAOImpl implements TicketDAO {

    @Override
    public Ticket get(int id) throws SQLException {
        String sql = "SELECT * FROM ticket WHERE id_ticket = ? ";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Ticket(
                    rs.getInt("id_ticket"),
                    rs.getInt("prix"),
                    rs.getInt("id_utilisateur"),
                    rs.getInt("id_match")
            );
        }
        return null;
    }

    @Override
    public List<Ticket> getAll() throws SQLException {
        String sql = "SELECT * FROM ticket";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return List.of(new Ticket(
                    rs.getInt("id_ticket"),
                    rs.getInt("prix"),
                    rs.getInt("id_utilisateur"),
                    rs.getInt("id_match")
            ));
        }
        return List.of();
    }

    @Override
    public int save(Ticket ticket) throws SQLException {
        if (ticket.getSerialNum() > 0) {
            return update(ticket);
        } else {
            return insert(ticket);
        }
    }

    @Override
    public int insert(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (prix, id_utilisateur, id_match) VALUES (?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, ticket.getPrice());
        ps.setInt(1, ticket.getSerialNum());
        ps.setInt(1, ticket.getMatchID());

        int affectedRows = ps.executeUpdate();
        if (affectedRows > 0) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public int update(Ticket ticket) throws SQLException {
        String sql = "UPDATE ticket SET prix = ?, id_utilisateur = ?, id_match = ? WHERE id_ticket = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ticket.getPrice());
        ps.setInt(1, ticket.getSerialNum());
        ps.setInt(1, ticket.getMatchID());
        return ps.executeUpdate();
    }

    @Override
    public int delete(Ticket ticket) {
        String sql = "DELETE FROM ticket WHERE id_ticket = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ticket.getSerialNum());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}