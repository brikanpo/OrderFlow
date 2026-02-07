package it.orderflow.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Supplier extends Contact {

    private BigDecimal transportFee;

    public Supplier(String name, String email) {
        super(email);
        this.changeName(name);
    }

    public Supplier(UUID id, String name, String email, String phone, BigDecimal transportFee) {
        super(id, name, email, phone);
        this.setTransportFee(transportFee);
    }

    public BigDecimal getTransportFee() {
        return this.transportFee;
    }

    private void setTransportFee(BigDecimal transportFee) {
        this.transportFee = transportFee;
    }

    public void changeTransportFee(BigDecimal transportFee) {
        this.setTransportFee(transportFee);
    }

    public Supplier copy() {
        return new Supplier(this.getId(), this.getName(), this.getEmail(), this.getPhone(), this.getTransportFee());
    }
}
