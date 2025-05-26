package model;

public class Ticket {
    private Integer serialNum;
    private Integer owner;
    private Integer matchID;
    private double price;
    private int stock;
    private ticketStatus status;
    private String seat;

    public enum ticketStatus {
        AVAILABLE, SOLD_OUT
    }

    public Ticket(Integer serialNum, Integer owner, Integer matchID, double price, int stock, ticketStatus status, String seat) {
        this.serialNum = serialNum;
        this.owner = owner;
        this.matchID = matchID;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.seat = seat;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ticketStatus getStatus() {
        return status;
    }

    public void setStatus(ticketStatus status) {
        this.status = status;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
