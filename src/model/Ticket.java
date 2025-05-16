package model;

public class Ticket {
    private int serialNum;
    private int price;
    private int owner;
    private int matchID;

    public Ticket(int serialNum, int price, int owner, int matchID) {
        this.serialNum = serialNum;
        this.price = price;
        this.owner = owner;
        this.matchID = matchID;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
}
