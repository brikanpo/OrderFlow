package it.orderflow.model;

import java.util.UUID;

public class Client extends Contact {

    private Address address;

    public Client(String name, String email) {
        super(email);
        this.changeName(name);
    }

    public Client(UUID id, String name, String email, String phone, Address address) {
        super(id, name, email, phone);
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    private void setAddress(Address address) {
        this.address = address;
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    public Client copy() {
        if (this.getAddress() == null) {
            return new Client(this.getId(), this.getName(), this.getEmail(), this.getPhone(), null);
        } else {
            return new Client(this.getId(), this.getName(), this.getEmail(), this.getPhone(), this.getAddress().copy());
        }
    }
}
