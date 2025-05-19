package model;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private Integer ticketSerial;
    private Integer customerId;
    private paymentMethod method;
    private paymentStatus status;
    private Timestamp paymentDate;

    public enum paymentStatus {
        PENDING, APPROVED, REJECTED
    }

    public enum paymentMethod {
        DEBIT, CREDIT
    }

    public Payment(int id, Integer ticketSerial, Integer customerId, paymentMethod method, paymentStatus status, Timestamp paymentDate) {
        this.id = id;
        this.ticketSerial = ticketSerial;
        this.customerId = customerId;
        this.method = paymentMethod.DEBIT;
        this.status = paymentStatus.PENDING;
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

    public paymentStatus getStatus() {
        return status;
    }

    public void setStatus(paymentStatus status) {
        this.status = status;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public paymentMethod getMethod() {
        return method;
    }

    public void setMethod(paymentMethod method) {
        this.method = method;
    }
}
