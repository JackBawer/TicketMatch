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

        initialize();
    }

    private void initialize() {
        setTitle("Add funds - " + user.getNom());
        setSize(600,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

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

        JLabel fundHeader = new JLabel("Add balance to your account");
        fundHeader.setFont(new Font("Arial", Font.BOLD, 20));
        fundHeader.setForeground(Color.WHITE);
        fundHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(fundHeader, BorderLayout.WEST);

        JButton cancelButton = new JButton("Go back");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setForeground(new Color(70, 130, 180));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel currentBalanceLabel = new JLabel("Current balance: " + user.getBalance() + "$");
        currentBalanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentBalanceLabel.setBackground(Color.WHITE);
        currentBalanceLabel.setForeground(new Color(70, 130, 180));
        currentBalanceLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(currentBalanceLabel);

        JPanel enterBalancePanel = new JPanel();
        enterBalancePanel.setBackground(Color.WHITE);
        enterBalancePanel.setLayout(new BoxLayout(enterBalancePanel, BoxLayout.X_AXIS));
        enterBalancePanel.setOpaque(false);
        enterBalancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        enterBalancePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(enterBalancePanel);

        JLabel enteredBalanceLabel = new JLabel("Enter Balance ($): ");
        enteredBalanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        enteredBalanceLabel.setBackground(Color.WHITE);
        enteredBalanceLabel.setForeground(new Color(70, 130, 180));
        enteredBalanceLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        enteredBalanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        enterBalancePanel.add(enteredBalanceLabel);

        //Number formatter
        NumberFormat format = NumberFormat.getNumberInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0);
        formatter.setMaximum(500.0);

        JFormattedTextField balanceField = new JFormattedTextField(formatter);
        balanceField.setValue(0.0);
        balanceField.setEditable(true);
        balanceField.setBackground(Color.WHITE);
        balanceField.setForeground(new Color(70, 130, 180));
        balanceField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        balanceField.setFont(new Font("Arial", Font.BOLD, 20));
        balanceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        enterBalancePanel.add(balanceField);

        JButton proceedButton = new JButton("Proceed");
        proceedButton.setFont(new Font("Arial", Font.BOLD, 23));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setBackground(new Color(70, 130, 180));
        proceedButton.setFocusPainted(false);
        proceedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        proceedButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        proceedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(proceedButton);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        //Action listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
                dispose();
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double funds = Double.parseDouble(balanceField.getText());
                if (funds == 0) {
                    JOptionPane.showMessageDialog(FundFrame.this,
                            "Please enter a valid number.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    ValidateFrame validateFrame = new ValidateFrame(user, funds);
                    validateFrame.setVisible(true);
                    dispose();
                }
            }
        });
    }
}