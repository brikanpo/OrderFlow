package it.orderflow.beans;

import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.Supplier;

import java.math.BigDecimal;
import java.util.UUID;

public class SupplierBean {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private BigDecimal transportFee;

    public SupplierBean() {
    }

    public SupplierBean(Supplier supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.email = supplier.getEmail();
        this.phone = supplier.getPhone();
        this.transportFee = supplier.getTransportFee();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws InvalidInputException {
        if (name.isBlank()) {
            throw new InvalidInputException(InvalidInputException.InputType.BLANK);
        }
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) throws InvalidInputException {
        if (email.isBlank() || !email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException(InvalidInputException.InputType.EMAIL);
        }
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getTransportFee() {
        return this.transportFee;
    }

    public void setTransportFee(BigDecimal transportFee) {
        this.transportFee = transportFee;
    }
}
