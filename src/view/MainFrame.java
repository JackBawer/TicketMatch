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

    public MainFrame(Utilisateur user) {
        this.user = user;
        this.matchDAO = new MatchDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("User Panel - " + user.getNom());
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));

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

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getNom() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
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
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Action panel
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(new Color(245, 245, 250));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.PAGE_AXIS));

        // Fund panel
        JPanel fundPanel = new JPanel();
        fundPanel.setLayout(new BoxLayout(fundPanel, BoxLayout.PAGE_AXIS));
        fundPanel.setBackground(new Color(245, 245, 250));
        fundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        actionPanel.add(fundPanel);

        JLabel JBalance = new JLabel("Current Balance: " + user.getBalance() + "$");
        JBalance.setFont(new Font("Arial", Font.BOLD, 14));
        JBalance.setForeground(new Color(70, 130, 180));
        JBalance.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        JBalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        fundPanel.add(JBalance);

        JPanel matchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matchPanel.setOpaque(false);
        matchPanel.setBackground(new Color(245, 245, 250));
        matchPanel.setBorder(BorderFactory.createEtchedBorder());

        JLabel matchList = new JLabel("Upcoming matches: ");
        matchList.setFont(new Font("Arial", Font.BOLD, 14));
        matchList.setBackground(Color.WHITE);
        matchList.setForeground(new Color(70, 130, 180));
        matchList.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchPanel.add(matchList, BorderLayout.NORTH);

        JButton fundButton = new JButton("Add funds");
        fundButton.setFont(new Font("Arial", Font.BOLD, 14));
        fundButton.setForeground(Color.WHITE);
        fundButton.setBackground(new Color(70, 130, 180));
        fundButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        fundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fundPanel.add(fundButton);

        JButton historyButton = new JButton("Purchase history");
        historyButton.setFont(new Font("Arial", Font.BOLD, 14));
        historyButton.setForeground(Color.WHITE);
        historyButton.setBackground(new Color(70, 130, 180));
        historyButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        historyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        historyButton.setFocusPainted(false);
        actionPanel.add(historyButton);

        JButton myTicketsButton = new JButton("My tickets");
        myTicketsButton.setFont(new Font("Arial", Font.BOLD, 14));
        myTicketsButton.setForeground(Color.WHITE);
        myTicketsButton.setBackground(new Color(70, 130, 180));
        myTicketsButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        myTicketsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        myTicketsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        myTicketsButton.setFocusPainted(false);
        actionPanel.add(myTicketsButton);

        JPanel matchItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matchItemsPanel.setLayout(new BoxLayout(matchItemsPanel, BoxLayout.PAGE_AXIS));

        try {
            assert matchDAO != null;
            List<Match> matchesList = matchDAO.getAll();
            for (Match match : matchesList) {
                JPanel matchItemPanel = new JPanel();
                matchItemPanel.setLayout(new BoxLayout(matchItemPanel, BoxLayout.X_AXIS));
                matchItemPanel.setBackground(new Color(245, 245, 250));
                matchItemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                matchItemPanel.setOpaque(false);
                matchItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel teams = new JLabel(match.getTeam1() + " - " + match.getTeam2());
                teams.setFont(new Font("Arial", Font.BOLD, 14));
                teams.setForeground(new Color(70, 130, 180));
                teams.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(teams);

                JLabel date = new JLabel(match.getMatchDate().toString());
                date.setFont(new Font("Arial", Font.BOLD, 14));
                date.setForeground(new Color(70, 130, 180));
                date.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(date);

                JLabel time = new JLabel(match.getMatchTime().toString());
                time.setFont(new Font("Arial", Font.BOLD, 14));
                time.setForeground(new Color(70, 130, 180));
                time.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(time);

                JLabel place = new JLabel(match.getLocation());
                place.setFont(new Font("Arial", Font.BOLD, 14));
                place.setForeground(new Color(70, 130, 180));
                place.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(place);

                JLabel capacity = new JLabel(Integer.toString(match.getStadiumCap()));
                capacity.setFont(new Font("Arial", Font.BOLD, 14));
                capacity.setForeground(new Color(70, 130, 180));
                capacity.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(capacity);

                JButton buyButton = new JButton("Buy");
                buyButton.setFont(new Font("Arial", Font.BOLD, 14));
                buyButton.setForeground(Color.WHITE);
                buyButton.setBackground(new Color(70, 130, 180));
                buyButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(buyButton, BorderLayout.EAST);

                buyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TicketValidateFrame validateFrame = new TicketValidateFrame(user);
                        validateFrame.setVisible(true);
                        dispose();
                    }
                });

                matchItemsPanel.add(matchItemPanel);
            }
        } catch (SQLException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        JPanel matchItem = new JPanel();
        matchItem.setLayout(new BoxLayout(matchItem, BoxLayout.Y_AXIS));
        matchItem.setBackground(new Color(245, 245, 250));
        matchItem.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        matchItemsPanel.add(matchItem);

        JScrollPane matchPanelScrollPane = new JScrollPane(matchItemsPanel);
        matchPanelScrollPane.setOpaque(false);
        matchPanelScrollPane.setBackground(new Color(245, 245, 250));
        matchPanelScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        matchPanelScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        matchPanel.add(matchPanelScrollPane, BorderLayout.CENTER);

        contentPanel.add(matchPanel, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.WEST);

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });

        fundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FundFrame fundFrame = new FundFrame(user);
                fundFrame.setVisible(true);
                dispose();
            }
        });

    }

    public static void main(String[] args) {
        AdminFrame adminFrame = new AdminFrame(new Utilisateur());
        adminFrame.setVisible(true);
    }
}