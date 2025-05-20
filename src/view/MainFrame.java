package view;

import model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements TaskManager.TaskChangeListener {
    private Utilisateur user;
    private TaskListPanel taskListPanel;
    private StatsPanel statsPanel;
    private ReminderManager reminderManager;
    private StatsDAO statsDAO;

    public MainFrame(Utilisateur user) {
        this.user = user;
        this.taskManager = new TaskManager(user.getId());
        this.statsDAO = new StatsDAO();
        this.reminderManager = new ReminderManager(taskManager);

        taskManager.addTaskChangeListener(this);

        initializeUI();

        // Start reminder checks
        reminderManager.startReminderChecks();
    }

    private void initializeUI() {
        setTitle("Task Manager - " + user.getUsername());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add window listener to stop reminder checks when closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                reminderManager.stopReminderChecks();
            }
        });

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

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!");
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

        // Create task list panel
        taskListPanel = new TaskListPanel(taskManager);

        // Create stats panel
        UserStats userStats = statsDAO.getStatsForUser(user.getId());
        statsPanel = new StatsPanel(userStats);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.addTab("Tasks", new ImageIcon(), taskListPanel, "Manage your tasks");
        tabbedPane.addTab("Statistics", new ImageIcon(), statsPanel, "View your statistics");

        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reminderManager.stopReminderChecks();
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }

    @Override
    public void onTasksChanged() {
        // Update stats panel when tasks change
        UserStats userStats = statsDAO.getStatsForUser(user.getId());
        statsPanel.updateStats(userStats);
    }
}