package view;

import model.AdminPanel;
import model.UserPanel;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private final Utilisateur user;
    private UserPanel userPanel;
    private AdminPanel adminPanel;
    private MatchPanel matchPanel;


    public MainFrame(Utilisateur user) {
        this.user = user;
        this.userPanel = new UserPanel();
        this.adminPanel = new AdminPanel();

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

        JPanel fundPanel = new JPanel();
        fundPanel.setLayout(new BoxLayout(fundPanel, BoxLayout.PAGE_AXIS));
        fundPanel.setBackground(new Color(245, 245, 250));
        fundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel JBalance = new JLabel("Current Balance: " + user.getBalance() + "$");
        JBalance.setFont(new Font("Arial", Font.BOLD, 14));
        JBalance.setForeground(new Color(70, 130, 180));
        JBalance.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        JBalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        fundPanel.add(JBalance);

        // Create task list panel
        matchPanel = new MatchPanel();

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setOpaque(false);
        actionPanel.setBackground(new Color(245, 245, 250));
        actionPanel.setBorder(BorderFactory.createEtchedBorder());

        JButton fundButton = new JButton("Add funds");
        fundButton.setFont(new Font("Arial", Font.BOLD, 14));
        fundButton.setForeground(Color.WHITE);
        fundButton.setBackground(new Color(70, 130, 180));
        fundButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        fundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fundPanel.add(fundButton);

        contentPanel.add(actionPanel, BorderLayout.CENTER);
        contentPanel.add(fundPanel, BorderLayout.WEST);

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
        MainFrame mainFrame = new MainFrame(new Utilisateur());
        mainFrame.setVisible(true);
    }
}