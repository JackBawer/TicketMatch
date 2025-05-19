package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public Payment get(int id) throws SQLException {
        String sql = "select * from paiement where id_paiement = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return new Payment(
                    rs.getInt("id_paiement"),
                    rs.getInt("id_ticket"),
                    rs.getInt("id_utilisateur"),
                    rs.getString("mode_paiement"),
                    rs.getString("statut"),
                    rs.getTimestamp("date_paiement")
            );
        }
        return null;
    }

    @Override
    public List<Payment> getAll() throws SQLException {
        String sql = "select * from paiement order by id_paiement";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Payment> list = new ArrayList<>();
        while(rs.next()) {
            list.add(new Payment(
                    rs.getInt("id_paiement"),
                    rs.getInt("id_ticket"),
                    rs.getInt("id_utilisateur"),
                    rs.getString("mode_paiement"),
                    rs.getString("statut"),
                    rs.getTimestamp("date_paiement")
            ));
        }
        return list;
    }

    @Override
    public int save(Payment payment) throws SQLException {
        if (payment.getId() > 0) {
            return update(payment);
        } else {
            return insert(payment);
        }
    }

    @Override
    public int insert(Payment payment) throws SQLException {
        String sql = "insert into paiement (id_paiement, id_ticket, id_utilisateur, mode_paiement, statut, date_paiement) values(?,?,?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, payment.getId());
        ps.setInt(2, payment.getTicketSerial());
        ps.setInt(3, payment.getCustomerId());
        ps.setString(4, payment.getPaymentMethod());
        ps.setString(5, payment.getStatus());
        ps.setTimestamp(6, payment.getPaymentDate());
        return 0;
    }

    @Override
    public int update(Payment payment) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Payment payment) {
        String sql = "delete from paiement where id_paiement = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, payment.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
