package view;

import model.Match;
import model.MatchDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class NewMatchFrame extends JFrame {
    private final AdminFrame adminFrame;
    MatchDAOImpl matchDAO;

    public NewMatchFrame(AdminFrame adminFrame) {
        this.adminFrame = adminFrame;
        matchDAO = new MatchDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Add new match");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        JLabel welcomeLabel = new JLabel("Add new match");
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
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel teamOnePanel = new JPanel();
        teamOnePanel.setLayout(new BoxLayout(teamOnePanel, BoxLayout.X_AXIS));

        JLabel teamOneLabel = new JLabel("Home team");
        teamOneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        teamOneLabel.setBackground(Color.WHITE);
        teamOneLabel.setForeground(Color.orange);
        teamOnePanel.add(teamOneLabel);

        JTextField teamOneField = new JTextField();
        teamOneField.setFont(new Font("Arial", Font.PLAIN, 14));
        teamOneField.setBackground(Color.WHITE);
        teamOneField.setForeground(Color.black);
        teamOnePanel.add(teamOneField);

        contentPanel.add(teamOnePanel);

        JPanel teamTwoPanel = new JPanel();
        teamTwoPanel.setLayout(new BoxLayout(teamTwoPanel, BoxLayout.X_AXIS));

        JLabel teamTwoLabel = new JLabel("Away team");
        teamTwoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        teamTwoLabel.setBackground(Color.WHITE);
        teamTwoLabel.setForeground(Color.orange);
        teamTwoPanel.add(teamTwoLabel);

        JTextField teamTwoField = new JTextField();
        teamTwoField.setFont(new Font("Arial", Font.PLAIN, 14));
        teamTwoField.setBackground(Color.WHITE);
        teamTwoField.setForeground(Color.black);
        teamTwoPanel.add(teamTwoField);

        contentPanel.add(teamTwoPanel);

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setBackground(Color.WHITE);
        dateLabel.setForeground(Color.orange);
        datePanel.add(dateLabel);

        JTextField dateField = new JTextField();
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dateField.setBackground(Color.WHITE);
        dateField.setForeground(Color.black);
        datePanel.add(dateField);

        contentPanel.add(datePanel);

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel.setBackground(Color.WHITE);
        timeLabel.setForeground(Color.orange);
        timePanel.add(timeLabel);

        JTextField timeField = new JTextField();
        timeField.setFont(new Font("Arial", Font.PLAIN, 14));
        timeField.setBackground(Color.WHITE);
        timeField.setForeground(Color.black);
        timePanel.add(timeField);

        contentPanel.add(timePanel);

        JPanel placePanel = new JPanel();
        placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.X_AXIS));

        JLabel placeLabel = new JLabel("Stadium");
        placeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        placeLabel.setBackground(Color.WHITE);
        placeLabel.setForeground(Color.orange);
        placePanel.add(placeLabel);

        JTextField placeField = new JTextField();
        placeField.setFont(new Font("Arial", Font.PLAIN, 14));
        placeField.setBackground(Color.WHITE);
        placeField.setForeground(Color.black);
        placePanel.add(placeField);

        contentPanel.add(placePanel);

        JPanel capacityPanel = new JPanel();
        capacityPanel.setLayout(new BoxLayout(capacityPanel, BoxLayout.X_AXIS));

        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        capacityLabel.setBackground(Color.WHITE);
        capacityLabel.setForeground(Color.orange);
        capacityPanel.add(capacityLabel);

        JTextField capacityField = new JTextField();
        capacityField.setFont(new Font("Arial", Font.PLAIN, 14));
        capacityField.setBackground(Color.WHITE);
        capacityField.setForeground(Color.black);
        capacityPanel.add(capacityField);

        contentPanel.add(capacityPanel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(Color.orange);
        confirmButton.setForeground(Color.white);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(confirmButton);

        add(mainPanel);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (teamOneField.getText().isEmpty() ||
                        teamTwoField.getText().isEmpty() ||
                        dateField.getText().isEmpty() ||
                        timeField.getText().isEmpty() ||
                        placeField.getText().isEmpty() ||
                        capacityField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(NewMatchFrame.this, "Please fill all fields!", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int matchID = ThreadLocalRandom.current().ints(0,999).distinct().limit(3).sum();
                        Match newMatch = new Match(matchID,
                                teamOneField.getText(),
                                teamTwoField.getText(),
                                LocalDate.parse(dateField.getText()),
                                LocalTime.parse(timeField.getText()),
                                placeField.getText(),
                                Integer.parseInt(capacityField.getText()));

                        matchDAO.insert(newMatch);
                        JOptionPane.showMessageDialog(NewMatchFrame.this,
                                "Match added!",
                                "Success!",
                                JOptionPane.INFORMATION_MESSAGE);
                        adminFrame.revalidate();
                        adminFrame.repaint();
                        dispose();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(NewMatchFrame.this,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}