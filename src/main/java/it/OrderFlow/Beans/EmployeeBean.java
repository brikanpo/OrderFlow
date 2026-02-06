package it.OrderFlow.Beans;

import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.Model.Employee;
import it.OrderFlow.Model.UserRole;

import java.util.UUID;

public class EmployeeBean {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserRole userRole;

    public EmployeeBean() {
    }

    public EmployeeBean(EmployeeBean employeeBean) {
        this.id = employeeBean.getId();
        this.name = employeeBean.getName();
        this.email = employeeBean.getEmail();
        this.phone = employeeBean.getPhone();
        this.userRole = employeeBean.getUserRole();
    }

    public EmployeeBean(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.userRole = employee.getUserRole();
    }

    public UUID getId() {
        return id;
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

    public UserRole getUserRole() {
        return this.userRole;
    }

    public void setUserRole(UserRole userRole) throws InvalidInputException {
        if (userRole == null) {
            throw new InvalidInputException(InvalidInputException.InputType.ROLE);
        }
        this.userRole = userRole;
    }
}
