package service;

import model.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaymentService {
    List<Payment> history;

    public PaymentService() {
        history = new ArrayList<Payment>();
    }

    TicketDAOImpl ticketDAO = new TicketDAOImpl();
    UtilisateurDAOImpl userDAO = new UtilisateurDAOImpl();
    PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

    public void purchaseTicket(int customerId, int ticketId) throws SQLException {
        Utilisateur user = userDAO.get(customerId);
        Ticket ticket = ticketDAO.get(ticketId);

        int paymentID = Integer.parseInt(String.valueOf(user.getIdUtilisateur()) + String.valueOf(ticketId) + new Random().nextInt(999));
        Payment payment = new Payment(paymentID, ticket.getSerialNum(), user.getIdUtilisateur(), Payment.paymentMethod.DEBIT, Payment.paymentStatus.PENDING, Timestamp.valueOf(LocalDateTime.now()));
        paymentDAO.insert(payment);

        if (user.getIdUtilisateur() == null) { // user does not exist
            payment.setStatus(Payment.paymentStatus.REJECTED);
            paymentDAO.update(payment);
            history.add(payment);
        }

        else if (ticket.getOwner() != null || ticket.getSerialNum() == null) { // ticket is used or doesn't exist
            payment.setStatus(Payment.paymentStatus.REJECTED);
            paymentDAO.update(payment);
            history.add(payment);
        }

        else if (user.getBalance() < ticket.getPrice()) { // insufficient funds
            payment.setStatus(Payment.paymentStatus.REJECTED);
            paymentDAO.update(payment);
            history.add(payment);
        }

        else {
            payment.setStatus(Payment.paymentStatus.APPROVED);
            System.out.println("Purchase successful!");
            deductFunds(user.getIdUtilisateur(), ticket.getPrice());
            updateStock(ticket.getSerialNum());
            paymentDAO.update(payment);
            history.add(payment);
        }
    }

    public void deductFunds(int id, double price) throws SQLException {
        Utilisateur user = userDAO.get(id);
        double difference = user.getBalance() - price;
        user.setBalance(difference);
        userDAO.update(user);
    }

    public List<Payment> paymentHistory(int customerID) {
        List<Payment> userHistory = new ArrayList<>();
        for (Payment payment : history) {
            if(payment.getCustomerId().equals(customerID)) {
                userHistory.add(payment);
            }
        }
        return userHistory;
    }

    public void updateStock(int ticketID) throws SQLException {
        Ticket ticket = ticketDAO.get(ticketID);
        ticket.setStock(ticket.getStock() - 1);
        ticketDAO.update(ticket);
    }
}