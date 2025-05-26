package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class TicketValidateFrame extends JFrame {
    private final Utilisateur user;
    private final Match match;
    TicketDAOImpl ticketDAO;
    UtilisateurDAOImpl userDAO;

    public TicketValidateFrame(Utilisateur user, Match match) {
        this.user = user;
        this.match = match;
        ticketDAO = new TicketDAOImpl();
        userDAO = new UtilisateurDAOImpl();
        initialize();
    }

    private void initialize() {
        this.setTitle("Complete opreation - " + user.getNom());
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
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.X_AXIS));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel seatLabel = new JLabel("Seat: ");
        seatLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        seatLabel.setForeground(Color.black);
        seatPanel.add(seatLabel);

        String[] options = {"Corner 1", "Corner 2", "Corner 3", "Corner 4", "Lateral 1", "Lateral 2", "Goal 1", "Goal 2"};

        JComboBox<String> seatComboBox = new JComboBox<>(options);
        seatComboBox.setBackground(Color.WHITE);
        seatPanel.add(seatComboBox);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(70, 130, 180));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(seatPanel);
        contentPanel.add(confirmButton);

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

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userID = user.getIdUtilisateur();
                int matchID = match.getIdMatch();
                System.out.println(userID);
                System.out.println(matchID);
                Ticket ticket = new Ticket(null, null, null, 5.00, match.getStadiumCap(), Ticket.ticketStatus.AVAILABLE, null);
                String seat = (String) seatComboBox.getSelectedItem();
                if (user.getBalance() < ticket.getPrice()) {
                    JOptionPane.showMessageDialog(TicketValidateFrame.this,
                            "Insufficient funds!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    ticket.setSerialNum(new Random().nextInt(match.getStadiumCap()));
                    ticket.setMatchID(matchID);
                    ticket.setOwner(userID);
                    ticket.setStatus(Ticket.ticketStatus.SOLD_OUT);
                    ticket.setSeat(seat);
                    try {
                        ticketDAO.insert(ticket);
                        JOptionPane.showMessageDialog(TicketValidateFrame.this,
                                "Transaction complete!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        user.setBalance(user.getBalance() - ticket.getPrice());
                        userDAO.update(user);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(TicketValidateFrame.this,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}