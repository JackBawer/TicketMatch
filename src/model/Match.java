package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Match {
    private int idMatch;
    private String team1;
    private String team2;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String location;
    private int stadiumCap;

    public Match() {

    }

    public Match(String team1, String team2, LocalDate matchDate, LocalTime matchTime, String location, int stadiumCap) {
        this.team1 = team1;
        this.team2 = team2;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.location = location;
        this.stadiumCap = stadiumCap;
    }

    public Match(int idMatch, String team1, String team2, LocalDate MatchDate, LocalTime MatchTime, String location, int stadiumCap) {
        this.idMatch = idMatch;
        this.team1 = team1;
        this.team2 = team2;
        this.matchDate = MatchDate;
        this.matchTime = MatchTime;
        this.location = location;
        this.stadiumCap = stadiumCap;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

   public LocalDate getMatchDate() {
        return matchDate;
   }

   public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
   }

   public LocalTime getMatchTime() {
        return matchTime;
   }

   public void setMatchTime(LocalTime matchTime) {
        this.matchTime = matchTime;
   }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStadiumCap() {
        return stadiumCap;
    }

    public void setStadiumCap(int stadiumCap) {
        this.stadiumCap = stadiumCap;
    }
}
