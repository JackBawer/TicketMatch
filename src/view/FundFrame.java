package view;

import model.Utilisateur;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class FundFrame extends JFrame {
    private final Utilisateur user;

    public FundFrame(Utilisateur user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Add Funds - " + user.getNom());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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

        JLabel titleLabel = new JLabel("Add Funds");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = createHeaderButton("Back");
        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Content panel with card styling
        JPanel contentPanel = createRoundedPanel(15, Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Current balance display
        JLabel currentBalanceLabel = new JLabel("Current Balance");
        currentBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currentBalanceLabel.setForeground(new Color(108, 117, 125));
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceAmount = new JLabel(String.format("$%.2f", user.getBalance()));
        balanceAmount.setFont(new Font("Segoe UI", Font.BOLD, 36));
        balanceAmount.setForeground(new Color(40, 167, 69));
        balanceAmount.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceAmount.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));

        // Amount input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel amountLabel = new JLabel("Amount to Add");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountLabel.setForeground(new Color(108, 117, 125));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Formatted input field
        NumberFormat format = NumberFormat.getNumberInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0);
        formatter.setMaximum(1000.0);

        JFormattedTextField amountField = new JFormattedTextField(formatter);
        amountField.setValue(0.0);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setMaximumSize(new Dimension(250, 50));
        amountField.putClientProperty("JTextField.placeholderText", "0.00");

        // Quick amount buttons
        JPanel quickAmountPanel = new JPanel();
        quickAmountPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        quickAmountPanel.setOpaque(false);
        quickAmountPanel.setMaximumSize(new Dimension(400, 60));

        String[] amounts = {"$10", "$25", "$50", "$100"};
        for (String amount : amounts) {
            JButton quickButton = new JButton(amount);
            quickButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            quickButton.setForeground(new Color(70, 130, 180));
            quickButton.setBackground(new Color(240, 248, 255));
            quickButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            quickButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            quickButton.addActionListener(e -> amountField.setValue(
                    Double.parseDouble(amount.substring(1))
            ));
            quickAmountPanel.add(quickButton);
        }

        // Submit button
        JButton submitButton = createStyledButton("Add Funds", new Color(70, 130, 180));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setMaximumSize(new Dimension(200, 50));
        submitButton.addActionListener(e -> {
            double funds = ((Number)amountField.getValue()).doubleValue();
            if (funds <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid amount greater than $0",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                FundValidateFrame fundValidateFrame = new FundValidateFrame(user, funds);
                fundValidateFrame.setVisible(true);
                dispose();
            }
        });

        // Add components to content panel
        contentPanel.add(currentBalanceLabel);
        contentPanel.add(balanceAmount);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(amountLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(amountField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(quickAmountPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(submitButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
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