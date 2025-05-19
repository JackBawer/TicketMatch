package model;

public class Ticket {
    private int serialNum;
    private Integer owner;
    private Integer matchID;
    private double price;
    private int stock;

    public Ticket(int serialNum, Integer owner, Integer matchID, double price, int stock) {
        this.serialNum = serialNum;
        this.owner = null;
        this.matchID = null;
        this.price = price;
        this.stock = stock;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
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
}
