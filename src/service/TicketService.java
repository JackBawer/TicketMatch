package service;

import model.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class TicketService {

    private TicketDAOImpl ticketDAO = new TicketDAOImpl();
    private UtilisateurDAOImpl userDAO = new UtilisateurDAOImpl();
    private PaymentService paymentService = new PaymentService();

    public List<Ticket> acheterTickets(int userId, int matchId, int quantity, Payment.paymentMethod method) throws SQLException {
        Utilisateur user = userDAO.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur inexistant.");
        }

        // 1. Récupérer les tickets disponibles
        List<Ticket> availableTickets = ticketDAO.findAvailableTickets(matchId);
        if (availableTickets.size() < quantity) {
            throw new RuntimeException("Pas assez de tickets disponibles.");
        }

        // 2. Sélectionner les N premiers
        List<Ticket> selected = availableTickets.subList(0, quantity);
        List<Ticket> confirmed = new ArrayList<>();

        for (Ticket t : selected) {
            t.setOwner(userId);
            t.setStatus(Ticket.ticketStatus.SOLD_OUT);
            ticketDAO.update(t);

            paymentService.effectuerPaiement(t, user, method);
            confirmed.add(t);
        }

        return confirmed;
    }

}
