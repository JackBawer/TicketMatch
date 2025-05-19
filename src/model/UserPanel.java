package model;

import service.MatchService;
import service.PaymentService;
import service.TicketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

public class UserPanel {
    UtilisateurDAO userDAO = new UtilisateurDAOImpl();
    TicketDAO ticketDAO = new TicketDAOImpl();

    MatchService matchService = new MatchService();
    TicketService ticketService = new TicketService();
    PaymentService paymentService = new PaymentService();

    List<Ticket> history = new ArrayList<>();

    boolean running = true;

    public void initiate(int id) throws SQLException {
        Utilisateur user = userDAO.get(id);
        System.out.println("Welcome, " + user.getNom()+"!");
        Scanner sc = new Scanner(System.in);

        while(running) {
            System.out.println("""
                            ========== Menu =========
                            1. Buy tickets
                            2. Add funds
                            3. Purchase history
                            4. Exit
                            """);
            System.out.println("Balance: $" + user.getBalance());
            int choice = sc.nextInt();
            switch(choice) {
                case 1:
                    buy(user.getIdUtilisateur());
                    break;
                case 2:
                    addFunds(user.getIdUtilisateur());
                    break;
                case 3:
                    purchaseHistory();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
            sc.close();
        }
    }

    public void buy(int id) throws SQLException {
        Utilisateur user = userDAO.get(id);
        List<Ticket> tickets = availableTickets();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the serial number for the ticket you wish to purchase: ");
        int serial = sc.nextInt();
        for(Ticket ticket : tickets) {
            if (serial == ticket.getSerialNum()) {
                if (user.getBalance() >= ticket.getPrice()) {
                    deductFunds(user.getIdUtilisateur(), ticket.getPrice());
                    ticket.setOwner(user.getIdUtilisateur());
                    System.out.println("Purchase complete!");
                    ticketDAO.update(ticket);
                    history.add(ticket);
                    break;
                } else {
                    System.out.println("Insufficient funds!");
                }
            } else {
                System.out.println("Ticket not found.");
            }
        }
    }

    public void addFunds(int id) throws SQLException {
        Utilisateur user = userDAO.get(id);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter amount to add: ");
        int amount = sc.nextInt();
        user.setBalance(user.getBalance() + amount);
        userDAO.update(user);
        sc.close();
    }

    public void purchaseHistory() {
        System.out.println("Purchase history: ");
        System.out.println(history.toString());
    }

    public void deductFunds(int id, double price) throws SQLException {
        Utilisateur user = userDAO.get(id);
        double difference = user.getBalance() - price;
        user.setBalance(difference);
        userDAO.update(user);
    }

    public List<Ticket> availableTickets() throws SQLException {
        List<Ticket> allTickets = ticketDAO.getAll();
        List<Ticket> availTickets = new ArrayList<>();
        for(Ticket ticket : allTickets) {
            if (ticket.getStock() > 0 && ticket.getOwner() != 0) {
                availTickets.add(ticket);
            }
        }
        return availTickets;
    }
}