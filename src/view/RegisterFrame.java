package view;

import model.Utilisateur;
import model.UtilisateurDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private final UtilisateurDAOImpl userDAO;

    public RegisterFrame() {
        userDAO = new UtilisateurDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Task Manager - Register");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(52, 152, 219), // Green
                        0, getHeight(), new Color(52, 152, 219) // Teal
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setOpaque(false);

        // Create a rounded panel for register form
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 20, 10);
        formPanel.add(titleLabel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField(15);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setForeground(Color.black);
        registerButton.setBackground(new Color(52, 152, 219)); // Green
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(Color.black);
        backButton.setBackground(new Color(52, 152, 219)); // Blue
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 15, 10);
        formPanel.add(buttonPanel, gbc);

        // Add form panel to register panel
        registerPanel.add(formPanel);

        // Add register panel to main panel
        mainPanel.add(registerPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Please fill in all fields",
                            "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Passwords do not match",
                            "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    if (userDAO.usernameExists(username)) {
                        JOptionPane.showMessageDialog(RegisterFrame.this,
                                "Username already exists",
                                "Registration Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (userDAO.emailExists(email)) {
                        JOptionPane.showMessageDialog(RegisterFrame.this,
                                "Email already exists",
                                "Registration Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException sqlx) {
                    sqlx.printStackTrace();
                }

                Utilisateur user = new Utilisateur(username, email, password, Utilisateur.userRole.USER, 0);

                try {

                boolean success = userDAO.register(user);

                if (success) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Registration successful! Please login.",
                            "Registration Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Registration failed. Please try again.",
                            "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException sqlx) {
                    sqlx.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.initializeUI();
    }
}