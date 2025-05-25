package view;

import model.Utilisateur;
import model.UtilisateurDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;

public class FundValidateFrame extends JFrame {
    private final Utilisateur user;
    private final double value;
    private UtilisateurDAOImpl userDAO = new UtilisateurDAOImpl();

    public FundValidateFrame(Utilisateur user, double value) {
        this.user = user;
        this.value = value;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Confirm Payment - " + user.getNom());
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main panel with subtle background
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

        // Header panel with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(100, 149, 237),
                        getWidth(), 0, new Color(70, 130, 180)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel titleLabel = new JLabel("Confirm Payment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = createHeaderButton("Back");
        backButton.addActionListener(e -> {
            new FundFrame(user).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Content panel with card styling
        JPanel contentPanel = createRoundedPanel(15, Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Payment details
        JLabel paymentLabel = new JLabel("Payment Details");
        paymentLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        paymentLabel.setForeground(new Color(70, 130, 180));
        paymentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        paymentLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Current balance
        JPanel currentBalancePanel = createDetailPanel("Current Balance",
                String.format("$%.2f", user.getBalance()));

        // Amount to add
        JPanel amountPanel = createDetailPanel("Amount to Add",
                String.format("+$%.2f", value), new Color(40, 167, 69));

        // New balance
        JPanel newBalancePanel = createDetailPanel("New Balance",
                String.format("$%.2f", user.getBalance() + value), new Color(70, 130, 180));

        // Confirm button
        JButton confirmButton = createStyledButton("Confirm Payment", new Color(70, 130, 180));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setMaximumSize(new Dimension(200, 50));
        confirmButton.addActionListener(e -> processPayment());

        // Add components to content panel
        contentPanel.add(paymentLabel);
        contentPanel.add(currentBalancePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(amountPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(newBalancePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(confirmButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createDetailPanel(String label, String value) {
        return createDetailPanel(label, value, new Color(108, 117, 125));
    }

    private JPanel createDetailPanel(String label, String value, Color valueColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(400, 30));

        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelComponent.setForeground(new Color(73, 80, 87));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueComponent.setForeground(valueColor);
        valueComponent.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        panel.add(Box.createHorizontalGlue());
        panel.add(labelComponent);
        panel.add(valueComponent);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private void processPayment() {
        user.setBalance(user.getBalance() + value);
        try {
            userDAO.update(user);
            JOptionPane.showMessageDialog(this,
                    "Payment processed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            new MainFrame(user).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error processing payment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(70, 130, 180));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
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
}