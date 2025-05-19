package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

public class UserPanel {
    UtilisateurDAO userDAO = new UtilisateurDAOImpl();
    TicketDAO ticketDAO = new TicketDAOImpl();

    public void initiate(int id) throws SQLException {
        Utilisateur user = userDAO.get(id);
        System.out.println("Welcome, " + user.getNom()+"!");
        Scanner sc = new Scanner(System.in);

        while(true) {
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
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
            sc.close();
        }
    }

    public void buy(int id) throws SQLException {
        List<Ticket> tickets = availableTickets();
        System.out.println("Availabe tickets: ");
        System.out.println(tickets);
    }

    public void addFunds() {

    }

    public void purchaseHistory() {

    }

    public void deductFunds(int id, double price) throws SQLException {
        Utilisateur user = userDAO.get(id);
        double difference = user.getBalance() - price;
        user.setBalance(difference);
        userDAO.update(user);
    }

    public List<Ticket> availableTickets() throws SQLException {
        List<Ticket> allTickets = ticketDAO.getAll();
        List<Ticket> availTickets = new ArrayList<Ticket>();
        for(Ticket ticket : allTickets) {
            if (ticket.getStock() > 0) {
                availTickets.add(ticket);
            }
        }
        return availTickets;
    }
}