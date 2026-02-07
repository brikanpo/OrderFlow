package it.orderflow.beans;

import it.orderflow.exceptions.InvalidInputException;

public class EmployeeAccessBean {

    private String email;
    private String password;

    public EmployeeAccessBean() {
        this.email = null;
        this.password = null;
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) throws InvalidInputException {
        if (password.isBlank()) {
            throw new InvalidInputException(InvalidInputException.InputType.BLANK);
        }
        this.password = password;
    }
}
