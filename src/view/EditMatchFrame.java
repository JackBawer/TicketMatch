package view;

import model.Match;
import model.MatchDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class EditMatchFrame extends JFrame {
    private final AdminFrame adminFrame;
    private final Match match;
    private MatchDAOImpl matchDAO;

    public EditMatchFrame(Match match, AdminFrame adminFrame) {
        this.adminFrame = adminFrame;
        this.match = match;
        this.matchDAO = new MatchDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Edit Match #" + match.getIdMatch());
        setSize(900, 600);
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
                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Header panel with admin color scheme
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(220, 53, 69),
                        getWidth(), 0, new Color(153, 0, 51)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel titleLabel = new JLabel("Edit Match");
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

        // Form panel with card styling
        JPanel formPanel = createRoundedPanel(15, Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Add form fields with better layout
        JTextField teamOneField = addFormField(formPanel, "Home Team:", match.getTeam1());
        JTextField teamTwoField = addFormField(formPanel, "Away Team:", match.getTeam2());
        JTextField dateField = addFormField(formPanel, "Date (YYYY-MM-DD):", match.getMatchDate().toString());
        JTextField timeField = addFormField(formPanel, "Time (HH:MM):", match.getMatchTime().toString());
        JTextField placeField = addFormField(formPanel, "Stadium:", match.getLocation());
        JTextField capacityField = addFormField(formPanel, "Capacity:", String.valueOf(match.getStadiumCap()));

        // Submit button
        JButton submitButton = createStyledButton("Save Changes", new Color(220, 53, 69));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setMaximumSize(new Dimension(200, 50));
        submitButton.addActionListener(e -> {
            try {
                validateAndUpdateMatch(
                        teamOneField.getText(),
                        teamTwoField.getText(),
                        dateField.getText(),
                        timeField.getText(),
                        placeField.getText(),
                        capacityField.getText()
                );
            } catch (NumberFormatException ex) {
                showError("Capacity must be a valid number");
            } catch (DateTimeParseException ex) {
                showError("Invalid date or time format. Please use YYYY-MM-DD and HH:MM");
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        });

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(submitButton);

        // Scroll pane for form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JTextField addFormField(JPanel panel, String labelText, String initialValue) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(73, 80, 87));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField textField = new JTextField(initialValue);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        fieldPanel.add(label);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldPanel.add(textField);

        panel.add(fieldPanel);

        return textField;
    }

    private void validateAndUpdateMatch(
            String team1, String team2, String date,
            String time, String location, String capacity
    ) throws SQLException, NumberFormatException, DateTimeParseException {

        // Validate fields
        if (team1.isEmpty() || team2.isEmpty() || date.isEmpty() ||
                time.isEmpty() || location.isEmpty() || capacity.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        int capacityValue = Integer.parseInt(capacity);
        if (capacityValue <= 0) {
            showError("Capacity must be a positive number");
            return;
        }

        // Update match
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setMatchDate(LocalDate.parse(date));
        match.setMatchTime(LocalTime.parse(time));
        match.setLocation(location);
        match.setStadiumCap(capacityValue);

        matchDAO.update(match);

        showSuccess("Match updated successfully!");
        adminFrame.revalidate();
        adminFrame.repaint();
        dispose();
    }

    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(220, 53, 69));
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}