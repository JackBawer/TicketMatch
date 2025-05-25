package model;

public class Ticket {
    private Integer serialNum;
    private Integer owner;
    private Integer matchID;
    private double price;
    private int stock;
    private ticketStatus status;

    public enum ticketStatus {
        AVAILABLE, SOLD_OUT
    }

    public Ticket(Integer serialNum, Integer owner, Integer matchID, double price, int stock, ticketStatus status) {
        this.serialNum = serialNum;
        this.owner = null;
        this.matchID = null;
        this.price = price;
        this.stock = stock;
        this.status = status;
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
}
