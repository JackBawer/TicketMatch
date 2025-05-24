package view;

import model.MatchDAOImpl;
import model.Ticket;
import model.TicketDAOImpl;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StatsFrame extends JFrame {
    TicketDAOImpl ticketDAO;
    MatchDAOImpl matchDAO;

    public StatsFrame() {
        matchDAO = new MatchDAOImpl();
        ticketDAO = new TicketDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Statistics");
        setSize(900, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);


        // Header panel
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.ORANGE,
                        getWidth(), 0, new Color(255,72,0)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel welcomeLabel = new JLabel("Global statistics");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Go Back");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(new Color(70, 130, 180));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.setBackground(new Color(245, 245, 250));
        ticketPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel ticketHeader = new JLabel("Total tickets sold");
        ticketHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketHeader.setForeground(Color.black);
        ticketHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ticketHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(ticketHeader);

        try {
            JLabel ticketLabel = new JLabel(String.valueOf(ticketsSold().size()));
            ticketLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            ticketLabel.setForeground(Color.black);
            ticketLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketPanel.add(ticketLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel revenuePanel = new JPanel();
        revenuePanel.setLayout(new BoxLayout(revenuePanel, BoxLayout.Y_AXIS));
        revenuePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        revenuePanel.setBackground(new Color(245, 245, 250));
        revenuePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel revenueHeader = new JLabel("Total revenue generated");
        revenueHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        revenueHeader.setForeground(Color.black);
        revenueHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        revenuePanel.add(revenueHeader);

        try {
            JLabel revenueLabel = new JLabel(String.valueOf(getRevenue()));
            revenueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            revenueLabel.setForeground(Color.black);
            revenueLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            revenuePanel.add(revenueLabel);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        contentPanel.add(ticketPanel);
        contentPanel.add(revenuePanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public List<Ticket> ticketsSold() throws SQLException {
       return ticketDAO.getAll();
    }

    public double getRevenue() throws SQLException {
        double revenue = 0;
        List<Ticket> tickets = ticketDAO.getAll();
        for (Ticket ticket : tickets) {
            revenue += ticket.getPrice();
        }
        return revenue;
    }
}
