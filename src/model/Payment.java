package model;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private Integer ticketSerial;
    private Integer customerId;
    private String paymentMethod;
    private String status;
    private Timestamp paymentDate;

    public Payment(int id, Integer ticketSerial, Integer customerId, String paymentMethod, String status, Timestamp paymentDate) {
        this.id = id;
        this.ticketSerial = null;
        this.customerId = null;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTicketSerial() {
        return ticketSerial;
    }

    public void setTicketSerial(Integer ticketSerial) {
        this.ticketSerial = ticketSerial;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}
