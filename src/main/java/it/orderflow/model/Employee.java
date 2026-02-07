package it.orderflow.model;

import java.util.UUID;

public class Employee extends Contact {

    private String passwordHash;
    private UserRole userRole;

    public Employee(String email, String password) {
        super(email);
        this.setPasswordHash(this.getHash(password));
    }

    public Employee(UUID id, String name, String email, String phone, String passwordHash, UserRole userRole) {
        super(id, name, email, phone);
        this.setPasswordHash(passwordHash);
        this.setUserRole(userRole);
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    private void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    private void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean authenticate(Employee employee) {
        return this.getPasswordHash().equals(employee.getPasswordHash());
    }

    public boolean hasDefaultPassword() {
        return this.getPasswordHash().equals(this.getHash(this.getEmail()));
    }

    public void changePassword(String newPassword) {
        this.setPasswordHash(this.getHash(newPassword));
    }

    public void changeRole(UserRole userRole) {
        this.setUserRole(userRole);
    }

    private String getHash(String password) {
        //hashing logic
        return password;
    }

    public Employee copy() {
        return new Employee(this.getId(), this.getName(), this.getEmail(), this.getPhone(), this.getPasswordHash(), this.getUserRole());
    }
}
