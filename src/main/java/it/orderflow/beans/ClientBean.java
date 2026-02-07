package it.orderflow.beans;

import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.Client;

import java.util.UUID;

public class ClientBean {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private AddressBean addressBean;

    public ClientBean() {
    }

    public ClientBean(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        if (client.getAddress() != null) this.addressBean = new AddressBean(client.getAddress());
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

    public AddressBean getAddressBean() {
        return this.addressBean;
    }

    public void setAddressBean(AddressBean addressBean) {
        this.addressBean = addressBean;
    }
}
