package view;

import model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import java.util.List;

public class TicketsFrame extends JFrame {
    Utilisateur user;
    TicketDAOImpl ticketDAO;
    MatchDAOImpl matchDAO;

    public TicketsFrame(Utilisateur user) {
        this.user = user;
        ticketDAO = new TicketDAOImpl();
        matchDAO = new MatchDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("My Tickets - " + user.getNom());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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

        JLabel fundHeader = new JLabel("My Tickets");
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

        JPanel ticketsListPanel = new JPanel();
        ticketsListPanel.setLayout(new BoxLayout(ticketsListPanel, BoxLayout.Y_AXIS));
        ticketsListPanel.setOpaque(false);

        JPanel ticketHeader = createRoundedPanel(10, new Color(0, 119, 182));
        ticketHeader.setLayout(new GridLayout(1, 7, 10, 0));
        ticketHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        ticketHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        String[] headers = {"Ticket ID", "Owner ID", "Match ID", "Date", "Time", "Venue", "Seat", ""};
        for (String header : headers) {
            JLabel headerLabel = new JLabel(header, SwingConstants.CENTER);
            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            headerLabel.setForeground(Color.WHITE);
            ticketHeader.add(headerLabel);
        }

        ticketsListPanel.add(ticketHeader);
        ticketsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        try {
            List<Ticket> userTickets = ticketDAO.getUserTickets(user.getIdUtilisateur());

            for (Ticket ticket : userTickets) {
                JPanel ticketItem = createRoundedPanel(10, Color.WHITE);
                ticketItem.setLayout(new GridLayout(1, 7, 10, 0));
                ticketItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                ticketItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                JLabel ticketID = new JLabel(ticket.getSerialNum().toString());
                ticketItem.add(ticketID);

                JLabel ownerID = new JLabel(ticket.getOwner().toString());
                ticketItem.add(ownerID);

                JLabel matchID = new JLabel(String.valueOf(ticket.getMatchID()));
                ticketItem.add(matchID);

                Match match = matchDAO.get(ticket.getMatchID());

                JLabel date = new JLabel(match.getMatchDate().toString());
                ticketItem.add(date);

                JLabel time = new JLabel(match.getMatchTime().toString());
                ticketItem.add(time);

                JLabel venue = new JLabel(match.getLocation());
                ticketItem.add(venue);

                JLabel seat = new JLabel(String.valueOf(ticket.getSeat()));
                ticketItem.add(seat);

                ticketsListPanel.add(ticketItem);
                ticketsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                JButton viewPDF = createStyledButton("View", new Color(0,119,182));
                ticketItem.add(viewPDF);

                viewPDF.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            PDFViewer pdfViewer = new PDFViewer(generatePDF(ticket, match, seat.getText()));
                            pdfViewer.setVisible(true);
                            dispose();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        contentPanel.add(ticketsListPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        pack();

        //Action listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
                dispose();
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
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

    public PDDocument generatePDF(Ticket ticket, Match match, String seat) throws IOException {
        String filename = "ticket.pdf";

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        contentStream.beginText();
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Ticket ID: " + ticket.getSerialNum());
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Owner ID: " + ticket.getOwner());
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Match ID: " + ticket.getMatchID());
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Date: " + match.getMatchDate());
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Time: " + match.getMatchTime());
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Venue: " + match.getLocation());
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Seat: " + seat);

        contentStream.endText();
        contentStream.close();

        document.save(filename);
        return document;
    }
}