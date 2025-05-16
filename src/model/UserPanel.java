package model;

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
    public void buy() {

    }
}