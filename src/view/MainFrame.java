package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private final Utilisateur user;
    MatchDAOImpl matchDAO;
    private JLabel balanceLabel;

    public MainFrame(Utilisateur user) {
        this.user = user;
        this.matchDAO = new MatchDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sports Ticket Booking - " + user.getNom());
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel with modern background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 248, 250));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Modern gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 119, 182),
                        getWidth(), 0, new Color(3, 4, 94)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getNom() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = createStyledButton("Logout", new Color(255, 255, 255, 150));
        logoutButton.setForeground(new Color(3, 4, 94));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);

        JPanel sidebarPanel = createRoundedPanel(15, new Color(255, 255, 255));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        JPanel userCard = createRoundedPanel(15, new Color(240, 248, 255));
        userCard.setLayout(new BoxLayout(userCard, BoxLayout.Y_AXIS));
        userCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        userCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userIcon = new JLabel(new ImageIcon("user_icon.png")); // Replace with actual icon
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        userCard.add(userIcon);

        balanceLabel = new JLabel("Balance: $" + user.getBalance());
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(0, 119, 182));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        userCard.add(balanceLabel);

        JButton fundButton = createStyledButton("Add Funds", new Color(0, 119, 182));
        fundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userCard.add(fundButton);

        sidebarPanel.add(userCard);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton historyButton = createStyledButton("Purchase History", new Color(0, 119, 182));
        historyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(historyButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton myTicketsButton = createStyledButton("My Tickets", new Color(0, 119, 182));
        myTicketsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(myTicketsButton);

        JPanel matchesPanel = new JPanel(new BorderLayout());
        matchesPanel.setOpaque(false);

        JLabel matchesTitle = new JLabel("Upcoming Matches");
        matchesTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        matchesTitle.setForeground(new Color(3, 4, 94));
        matchesTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        matchesPanel.add(matchesTitle, BorderLayout.NORTH);

        JPanel matchesListPanel = new JPanel();
        matchesListPanel.setLayout(new BoxLayout(matchesListPanel, BoxLayout.Y_AXIS));
        matchesListPanel.setOpaque(false);

        JPanel matchHeader = createRoundedPanel(10, new Color(0, 119, 182));
        matchHeader.setLayout(new GridLayout(1, 7, 10, 0));
        matchHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        matchHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        String[] headers = {"Home Team", "Away Team", "Date", "Time", "Venue", "Capacity", ""};
        for (String header : headers) {
            JLabel headerLabel = new JLabel(header, SwingConstants.CENTER);
            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            headerLabel.setForeground(Color.WHITE);
            matchHeader.add(headerLabel);
        }

        matchesListPanel.add(matchHeader);
        matchesListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        try {
            List<Match> matchesList = matchDAO.getAll();
            for (Match match : matchesList) {
                JPanel matchItem = createRoundedPanel(10, Color.WHITE);
                matchItem.setLayout(new GridLayout(1, 7, 10, 0));
                matchItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                matchItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                // Home team
                JLabel team1 = createMatchLabel(match.getTeam1());
                matchItem.add(team1);

                // Away team
                JLabel team2 = createMatchLabel(match.getTeam2());
                matchItem.add(team2);

                JLabel date = createMatchLabel(match.getMatchDate().toString());
                matchItem.add(date);

                JLabel time = createMatchLabel(match.getMatchTime().toString());
                matchItem.add(time);

                JLabel venue = createMatchLabel(match.getLocation());
                matchItem.add(venue);

                JLabel capacity = createMatchLabel(Integer.toString(match.getStadiumCap()));
                matchItem.add(capacity);

                JButton buyButton = createStyledButton("Buy Tickets", new Color(72, 202, 118));
                buyButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                buyButton.addActionListener(e -> {
                    TicketValidateFrame validateFrame = new TicketValidateFrame(user, match);
                    validateFrame.setVisible(true);
                    dispose();
                });
                matchItem.add(buyButton);

                matchesListPanel.add(matchItem);
                matchesListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (SQLException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Error loading matches: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(matchesListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        matchesPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(sidebarPanel, BorderLayout.WEST);
        contentPanel.add(matchesPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Action listeners
        logoutButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        });

        fundButton.addActionListener(e -> {
            FundFrame fundFrame = new FundFrame(user);
            fundFrame.setVisible(true);
            dispose();
        });

        myTicketsButton.addActionListener(e -> {
            TicketsFrame ticketsFrame = new TicketsFrame(user);
            ticketsFrame.setVisible(true);
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        return button;
    }

    private JPanel createRoundedPanel(int radius, Color bgColor) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            }
        };
    }

    private JLabel createMatchLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(51, 51, 51));
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(new Utilisateur());
            mainFrame.setVisible(true);
        });
    }
}