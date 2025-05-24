package view;

import model.Match;
import model.MatchDAOImpl;
import model.Ticket;
import model.TicketDAOImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatchStatsFrame extends JFrame {
    private final Match match;
    MatchDAOImpl matchDAO;
    TicketDAOImpl ticketDAO;

    public MatchStatsFrame(Match match) {
        this.match = match;
        matchDAO = new MatchDAOImpl();
        ticketDAO = new TicketDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Statistics for Match " + match.getIdMatch());
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

        JLabel welcomeLabel = new JLabel("Match Statistics");
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
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel teamsPanel = new JPanel();
        teamsPanel.setLayout(new BoxLayout(teamsPanel, BoxLayout.Y_AXIS));
        teamsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        teamsPanel.setBackground(new Color(245, 245, 250));
        teamsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel teamsHeader = new JLabel("Teams");
        teamsHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        teamsHeader.setForeground(Color.black);
        teamsHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        teamsHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        teamsPanel.add(teamsHeader);

        JLabel teamsLabel = new JLabel(match.getTeam1() + " - " + match.getTeam2());
        teamsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        teamsLabel.setForeground(Color.black);
        teamsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        teamsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        teamsPanel.add(teamsLabel);

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        datePanel.setBackground(new Color(245, 245, 250));
        datePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel dateHeader = new JLabel("Date");
        dateHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        dateHeader.setForeground(Color.black);
        dateHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        dateHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        datePanel.add(dateHeader);

        JLabel dateLabel = new JLabel(match.getMatchDate().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(Color.black);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        datePanel.add(dateLabel);

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timePanel.setBackground(new Color(245, 245, 250));
        timePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel timeHeader = new JLabel("Time");
        timeHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        timeHeader.setForeground(Color.black);
        timeHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        timeHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        timePanel.add(timeHeader);

        JLabel timeLabel = new JLabel(match.getMatchTime().toString());
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(Color.black);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        timePanel.add(timeLabel);

        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.setBackground(new Color(245, 245, 250));
        ticketPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel ticketHeader = new JLabel("Tickets sold");
        ticketHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketHeader.setForeground(Color.black);
        ticketHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ticketHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(ticketHeader);


        JLabel ticketLabel = new JLabel(String.valueOf(ticketsSold(match).size()));
        ticketLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketLabel.setForeground(Color.black);
        ticketLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(ticketLabel);

        JPanel revenuePanel = new JPanel();
        revenuePanel.setLayout(new BoxLayout(revenuePanel, BoxLayout.Y_AXIS));
        revenuePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        revenuePanel.setBackground(new Color(245, 245, 250));
        revenuePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel revenueHeader = new JLabel("Revenue ($)");
        revenueHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        revenueHeader.setForeground(Color.black);
        revenueHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        revenueHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        revenuePanel.add(revenueHeader);

        JLabel revenueLabel = new JLabel(String.valueOf(getRevenueGenerated(match)));
        revenueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        revenueLabel.setForeground(Color.black);
        revenueLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        revenueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        revenuePanel.add(revenueLabel);

        contentPanel.add(teamsPanel);
        contentPanel.add(datePanel);
        contentPanel.add(timePanel);
        contentPanel.add(ticketPanel);
        contentPanel.add(revenuePanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public List<Ticket> ticketsSold(Match match) {
        try {
            List<Ticket> allTickets = ticketDAO.getAll();
            List<Ticket> soldTickets = new ArrayList<>();
            for (Ticket ticket : allTickets) {
                if (match.getIdMatch() == ticket.getMatchID()) {
                    soldTickets.add(ticket);
                }
            }
            return soldTickets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getRevenueGenerated(Match match) {
        double revenue = 0;
        List<Ticket> tickets = ticketsSold(match);
        for (Ticket ticket : tickets) {
            revenue = revenue + ticket.getPrice();
        }
        return revenue;
    }
}