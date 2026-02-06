package it.OrderFlow.Model;

import java.util.UUID;

public class Contact {

    private UUID id;
    private String name;
    private String email;
    private String phone;

    public Contact(String email) {
        this.id = UUID.randomUUID();
        this.name = null;
        this.email = email;
        this.phone = null;
    }

    public Contact(UUID id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UUID getId() {
        return this.id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    private void setPhone(String phone) {
        this.phone = phone;
    }

    public void changeName(String name) {
        this.setName(name);
    }

    public void changeEmail(String email) {
        this.setEmail(email);
    }

    public void changePhone(String phone) {
        this.setPhone(phone);
    }
}
