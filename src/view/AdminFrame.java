package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminFrame extends JFrame {
    private final Utilisateur user;
    MatchDAOImpl matchDAO;
    TicketDAOImpl ticketDAO;

    public AdminFrame(Utilisateur user) {
        this.user = user;
        matchDAO = new MatchDAOImpl();
        ticketDAO = new TicketDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - " + user.getNom());
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel with subtle background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Modern header with admin-specific colors
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Professional admin gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(220, 53, 69),
                        getWidth(), 0, new Color(153, 0, 51)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Welcome label with admin styling
        JLabel welcomeLabel = new JLabel("Admin Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Logout button with modern style
        JButton logoutButton = createStyledButton("Logout", new Color(255, 255, 255, 180));
        logoutButton.setForeground(new Color(153, 0, 51));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Content panel with card layout
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);

        // Sidebar panel with admin actions
        JPanel sidebarPanel = createRoundedPanel(15, new Color(255, 255, 255));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        // Admin action buttons
        JButton addNewMatchButton = createStyledButton("Add New Match", new Color(220, 53, 69));
        addNewMatchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(addNewMatchButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Quick stats panel
        JPanel statsPanel = createRoundedPanel(15, new Color(248, 249, 250));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statsTitle = new JLabel("Quick Stats");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsTitle.setForeground(new Color(73, 80, 87));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.add(statsTitle);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add some sample stats (would be populated with real data)
        addStatItem(statsPanel, "Total Matches", String.valueOf(getMatchCount()));
        addStatItem(statsPanel, "Tickets Sold", String.valueOf(ticketsSold()));
        addStatItem(statsPanel, "Revenue", String.valueOf(getRevenue()));

        sidebarPanel.add(statsPanel);

        // Main content area - matches list
        JPanel matchesPanel = new JPanel(new BorderLayout());
        matchesPanel.setOpaque(false);

        JLabel matchesTitle = new JLabel("Manage Matches");
        matchesTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        matchesTitle.setForeground(new Color(73, 80, 87));
        matchesTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        matchesPanel.add(matchesTitle, BorderLayout.NORTH);

        JPanel matchesListPanel = new JPanel();
        matchesListPanel.setLayout(new BoxLayout(matchesListPanel, BoxLayout.Y_AXIS));
        matchesListPanel.setOpaque(false);

        // Match table header
        JPanel matchHeader = createRoundedPanel(10, new Color(73, 80, 87));
        matchHeader.setLayout(new GridLayout(1, 9, 10, 0));
        matchHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        matchHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        String[] headers = {"Home Team", "Away Team", "Date", "Time", "Venue", "Capacity", "Edit", "Stats", "Action"};
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
                matchItem.setLayout(new GridLayout(1, 9, 10, 0));
                matchItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                matchItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                // Match details
                matchItem.add(createMatchLabel(match.getTeam1(), Font.BOLD));
                matchItem.add(createMatchLabel(match.getTeam2(), Font.BOLD));
                matchItem.add(createMatchLabel(match.getMatchDate().toString(), Font.PLAIN));
                matchItem.add(createMatchLabel(match.getMatchTime().toString(), Font.PLAIN));
                matchItem.add(createMatchLabel(match.getLocation(), Font.PLAIN));
                matchItem.add(createMatchLabel(Integer.toString(match.getStadiumCap()), Font.PLAIN));

                // Action buttons
                JButton editButton = createSmallButton("Edit", new Color(13, 110, 253));
                editButton.addActionListener(e -> {
                    EditMatchFrame editMatchFrame = new EditMatchFrame(match, AdminFrame.this);
                    editMatchFrame.setVisible(true);
                });
                matchItem.add(editButton);

                JButton statsButton = createSmallButton("Stats", new Color(108, 117, 125));
                statsButton.addActionListener(e -> {
                    MatchStatsFrame matchStatsFrame = new MatchStatsFrame(match);
                    matchStatsFrame.setVisible(true);
                });
                matchItem.add(statsButton);

                JButton deleteButton = createSmallButton("Delete", new Color(220, 53, 69));
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            AdminFrame.this,
                            "Are you sure you want to delete this match?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            matchDAO.delete(match);
                            matchesListPanel.remove(matchItem);
                            matchesListPanel.revalidate();
                            matchesListPanel.repaint();
                            JOptionPane.showMessageDialog(
                                    AdminFrame.this,
                                    "Match deleted successfully",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                    AdminFrame.this,
                                    "Error deleting match: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });
                matchItem.add(deleteButton);

                matchesListPanel.add(matchItem);
                matchesListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } catch (SQLException | NullPointerException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading matches: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JScrollPane scrollPane = new JScrollPane(matchesListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

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

        addNewMatchButton.addActionListener(e -> {
            NewMatchFrame newMatchFrame = new NewMatchFrame(AdminFrame.this);
            newMatchFrame.setVisible(true);
        });

    }

    // Helper methods
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
        return button;
    }

    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    private JLabel createMatchLabel(String text, int fontStyle) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", fontStyle, 14));
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    private void addStatItem(JPanel panel, String title, String value) {
        JPanel statItem = new JPanel(new BorderLayout());
        statItem.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(108, 117, 125));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(new Color(33, 37, 41));

        statItem.add(titleLabel, BorderLayout.NORTH);
        statItem.add(valueLabel, BorderLayout.SOUTH);
        statItem.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        panel.add(statItem);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            AdminFrame adminFrame = new AdminFrame(new Utilisateur());
            adminFrame.setVisible(true);
        });
    }

    public double getRevenue() {
        try {
            double revenue = 0;
            List<Ticket> tickets = ticketDAO.getAll();
            for (Ticket ticket : tickets) {
                revenue += ticket.getPrice();
            }
            return revenue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMatchCount() {
        try {
            return matchDAO.getAll().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int ticketsSold() {
        try {
            return ticketDAO.getAll().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}