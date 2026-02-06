package it.OrderFlow.Model;

import java.util.UUID;

public class Employee extends Contact implements Cloneable {

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
        //TODO hashing logic
        return password;
    }

    @Override
    public Employee clone() {
        try {
            return (Employee) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
