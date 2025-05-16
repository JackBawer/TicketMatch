package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class AdminPanel {
    MatchDAO matchDAO = new MatchDAOImpl();

    public void initiate() {
        while(true) {
            System.out.println("""
                    === Admin Panel ===
                    Select an option:
                    1) View all matches
                    2) Add new match
                    3) Edit match
                    4) Remove match
                    5) Exit
                    """);

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    viewAll();
                    break;
                case 2:
                    addNew();
                    break;
                case 3:
                    editMatch();
                    break;
                case 4:
                    removeMatch();
                    break;
                case 5:
                return;
                default:
                    System.out.println("Invalid option!");
            }
            scanner.close();
        }
    }

    private void viewAll() {
        System.out.println("==All matches==");
        try {
            System.out.println(matchDAO.getAll());
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNew() {
        Scanner scan = new Scanner(System.in);
        Match match = new Match();

        System.out.println("Enter team 1 name:");
        String homeTeam = scan.nextLine().trim();
        match.setTeam1(homeTeam);
        System.out.println("Enter team 2 name:");
        String awayTeam = scan.nextLine().trim();
        match.setTeam2(awayTeam);
        System.out.println("Enter match date (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(scan.nextLine().trim());
        match.setMatchDate(date);
        System.out.println("Enter match time (HH:mm):");
        LocalTime time = LocalTime.parse(scan.nextLine().trim());
        match.setMatchTime(time);
        System.out.println("Enter location:");
        String location = scan.nextLine().trim();
        match.setLocation(location);
        System.out.println("Enter stadium capacity:");
        int stadium = scan.nextInt();
        match.setStadiumCap(stadium);

        try {
                matchDAO.insert(match);
                matchDAO.save(match);
                System.out.println("Match added successfully!");
            } catch (SQLException e) {
            throw new RuntimeException(e);
            }
    }

    private void editMatch() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter match ID to edit: ");
        int matchId = scan.nextInt();

        try {
                Match match = matchDAO.get(matchId);
                matchDAO.update(match);
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeMatch() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter match ID to remove: ");
        int matchId = scan.nextInt();

        try {
                Match match = matchDAO.get(matchId);
                matchDAO.delete(match);
            System.out.println("Match removed successfully.");
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
