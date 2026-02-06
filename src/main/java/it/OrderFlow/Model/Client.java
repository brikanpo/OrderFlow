package it.OrderFlow.Model;

import java.util.UUID;

public class Client extends Contact implements Cloneable {

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

    @Override
    public Client clone() {
        try {
            Client clone = (Client) super.clone();
            if (this.address != null) clone.address = this.address.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
