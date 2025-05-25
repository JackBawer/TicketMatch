package view;

import model.DatabaseConnection;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TicketsFrame extends JFrame {
    Utilisateur user;

    public TicketsFrame(Utilisateur user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("My Tickets - " + user.getNom());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(100, 149, 237), // Cornflower blue
                        getWidth(), 0, new Color(70, 130, 180) // Steel blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel fundHeader = new JLabel("My Tickets");
        fundHeader.setFont(new Font("Arial", Font.BOLD, 20));
        fundHeader.setForeground(Color.WHITE);
        fundHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(fundHeader, BorderLayout.WEST);

        JButton cancelButton = new JButton("Go back");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setForeground(new Color(70, 130, 180));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTable purchaseHistoryTable = new JTable();
        purchaseHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        purchaseHistoryTable.setAutoCreateRowSorter(true);
        purchaseHistoryTable.getTableHeader().setReorderingAllowed(false);
        purchaseHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        try {
            fillTable((DefaultTableModel)purchaseHistoryTable.getModel());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane purchaseHistoryScrollPane = new JScrollPane(purchaseHistoryTable);
        contentPanel.add(purchaseHistoryScrollPane);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        //Action listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public void fillTable(DefaultTableModel tableModel) throws SQLException {
        String query = "SELECT * FROM ticket WHERE id_utilisateur = ?";
        Connection conn  = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, user.getIdUtilisateur());
        ResultSet rs = ps.executeQuery();

        tableModel.setRowCount(0);
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getInt("id_ticket"));
            row.add(rs.getInt("id_utilisateur"));
            row.add(rs.getInt("id_match"));
            row.add(rs.getInt("prix"));
            row.add(rs.getInt("stock"));
            row.add(rs.getString("statut"));
            tableModel.addRow(row);
        }
    }
}