package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AdminFrame extends JFrame {
    private final Utilisateur user;
    MatchDAOImpl matchDAO;

    public AdminFrame(Utilisateur user) {
        this.user = user;
        this.matchDAO = new MatchDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Panel - " + user.getNom());
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
                        0, 0, Color.ORANGE,
                        getWidth(), 0, new Color(255,72,0)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
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

        JButton addNewMatchButton = new JButton("Add new match");
        addNewMatchButton.setFont(new Font("Arial", Font.BOLD, 14));
        addNewMatchButton.setForeground(Color.WHITE);
        addNewMatchButton.setBackground(Color.orange);
        addNewMatchButton.setFocusPainted(false);
        addNewMatchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        addNewMatchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionPanel.add(addNewMatchButton);

        JButton globalStatsButton = new JButton("View statistics");
        globalStatsButton.setFont(new Font("Arial", Font.BOLD, 14));
        globalStatsButton.setForeground(Color.WHITE);
        globalStatsButton.setBackground(Color.orange);
        globalStatsButton.setFocusPainted(false);
        globalStatsButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        globalStatsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionPanel.add(globalStatsButton);

        JPanel matchItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matchItemsPanel.setLayout(new BoxLayout(matchItemsPanel, BoxLayout.PAGE_AXIS));

        JPanel matchHeaderPanel = new JPanel();
        matchHeaderPanel.setLayout(new BoxLayout(matchHeaderPanel, BoxLayout.X_AXIS));
        matchHeaderPanel.setBackground(new Color(245, 245, 250));
        matchHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        matchHeaderPanel.setOpaque(false);
        matchHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel teamOneHeader = new JLabel("Home team");
        teamOneHeader.setFont(new Font("Arial", Font.BOLD, 14));
        teamOneHeader.setForeground(new Color(70, 130, 180));
        teamOneHeader.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchHeaderPanel.add(teamOneHeader);

        JLabel teamTwoHeader = new JLabel("Away team");
        teamTwoHeader.setFont(new Font("Arial", Font.BOLD, 14));
        teamTwoHeader.setForeground(new Color(70, 130, 180));
        teamTwoHeader.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchHeaderPanel.add(teamTwoHeader);

        JLabel dateHeader = new JLabel("Date");
        dateHeader.setFont(new Font("Arial", Font.BOLD, 14));
        dateHeader.setForeground(new Color(70, 130, 180));
        dateHeader.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchHeaderPanel.add(dateHeader);

        JLabel timeHeader = new JLabel("Time");
        timeHeader.setFont(new Font("Arial", Font.BOLD, 14));
        timeHeader.setForeground(new Color(70, 130, 180));
        timeHeader.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchHeaderPanel.add(timeHeader);

        JLabel placeHeader = new JLabel("Place");
        placeHeader.setFont(new Font("Arial", Font.BOLD, 14));
        placeHeader.setForeground(new Color(70, 130, 180));
        placeHeader.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchHeaderPanel.add(placeHeader);

        JLabel capacityHeader = new JLabel("Capacity");
        capacityHeader.setFont(new Font("Arial", Font.BOLD, 14));
        capacityHeader.setForeground(new Color(70, 130, 180));
        capacityHeader.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        matchHeaderPanel.add(capacityHeader);

        matchItemsPanel.add(matchHeaderPanel);

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

                JLabel team1 = new JLabel(match.getTeam1());
                team1.setFont(new Font("Arial", Font.BOLD, 14));
                team1.setForeground(new Color(70, 130, 180));
                team1.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(team1);

                JLabel team2 = new JLabel(match.getTeam2());
                team2.setFont(new Font("Arial", Font.BOLD, 14));
                team2.setForeground(new Color(70, 130, 180));
                team2.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(team2);

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

                JButton editButton = new JButton("Edit");
                editButton.setFont(new Font("Arial", Font.BOLD, 14));
                editButton.setForeground(Color.WHITE);
                editButton.setBackground(Color.orange);
                editButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                matchItemPanel.add(editButton, BorderLayout.EAST);

                JButton viewStatsButton = new JButton("Statistics");
                viewStatsButton.setFont(new Font("Arial", Font.BOLD, 14));
                viewStatsButton.setForeground(Color.WHITE);
                viewStatsButton.setBackground(Color.orange);
                viewStatsButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                viewStatsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                matchItemPanel.add(viewStatsButton);

                JButton deleteMatchButton = new JButton("Delete");
                deleteMatchButton.setFont(new Font("Arial", Font.BOLD, 14));
                deleteMatchButton.setForeground(Color.WHITE);
                deleteMatchButton.setBackground(Color.orange);
                deleteMatchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                deleteMatchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                matchItemPanel.add(deleteMatchButton);

                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EditMatchFrame editMatchFrame = new EditMatchFrame(match, AdminFrame.this);
                        editMatchFrame.setVisible(true);
                    }
                });

                viewStatsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MatchStatsFrame matchStatsFrame = new MatchStatsFrame(match);
                        matchStatsFrame.setVisible(true);
                    }
                });

                deleteMatchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        matchItemsPanel.remove(matchItemPanel);
                        matchDAO.delete(match);
                        revalidate();
                        repaint();
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

        addNewMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMatchFrame newMatchFrame = new NewMatchFrame(AdminFrame.this);
                newMatchFrame.setVisible(true);
            }
        });

        globalStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatsFrame statsFrame = new StatsFrame();
                statsFrame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        UIManager.put("Button.arc", 999);

        AdminFrame adminFrame = new AdminFrame(new Utilisateur());
        adminFrame.setVisible(true);
    }
}