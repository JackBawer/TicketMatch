package view;

import model.Utilisateur;
import model.UtilisateurDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FundValidateFrame extends JFrame {
    private final Utilisateur user;
    private final double value;

    UtilisateurDAOImpl userDAO = new UtilisateurDAOImpl();

    public FundValidateFrame(Utilisateur user, double value) {
        this.user = user;
        this.value = value;
        initialize();
    }

    public void initialize(){
        setTitle("Confirm Operation - " + user.getNom());
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JLabel fundHeader = new JLabel("Confirm operation");
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

        JLabel newBalanceLabel = new JLabel("New balance after operation: " + (user.getBalance()+value) + "$");
        newBalanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newBalanceLabel.setBackground(Color.WHITE);
        newBalanceLabel.setForeground(new Color(70, 130, 180));
        newBalanceLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        newBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(newBalanceLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(70, 130, 180));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(confirmButton);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        //Action listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FundFrame fundFrame = new FundFrame(user);
                fundFrame.setVisible(true);
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.setBalance(user.getBalance()+value);
                try {
                    userDAO.update(user);
                    JOptionPane.showMessageDialog(FundValidateFrame.this,
                            "Operation successful!",
                            "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                    MainFrame mainFrame = new MainFrame(user);
                    mainFrame.setVisible(true);
                    dispose();
                } catch (SQLException sqlx) {
                    JOptionPane.showMessageDialog(FundValidateFrame.this,
                            sqlx.getMessage(),
                            "Error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
