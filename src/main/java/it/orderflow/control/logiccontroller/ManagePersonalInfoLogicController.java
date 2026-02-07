package it.orderflow.control.logiccontroller;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.ControllerEvent;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.EmployeeDAO;
import it.orderflow.exceptions.EntityException;
import it.orderflow.exceptions.EntityNotFoundException;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.Employee;

import java.util.List;

public class ManagePersonalInfoLogicController extends TransactionSafeController {

    private final EmployeeDAO employeeDAO;
    private EmployeeBean loggedEmployee;

    public ManagePersonalInfoLogicController(EmployeeDAO employeeDAO, EmployeeBean loggedEmployee) {
        this.employeeDAO = employeeDAO;
        this.loggedEmployee = loggedEmployee;
    }

    public EmployeeDAO getEmployeeDAO() {
        return this.employeeDAO;
    }

    public EmployeeBean getLoggedEmployee() {
        return this.loggedEmployee;
    }

    public void setLoggedEmployee(EmployeeBean loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
    }

    public boolean hasDefaultPassword() throws PersistenceException {
        Employee targetEmployee = this.getEmployeeDAO().loadEmployee(this.getLoggedEmployee().getEmail());
        return targetEmployee.hasDefaultPassword();
    }

    public void saveNewPassword(EmployeeAccessBean employeeAccessBean)
            throws EntityNotFoundException, InvalidInputException, PersistenceException {
        if (!employeeAccessBean.getPassword().isBlank()) {
            Employee oldEmployee = this.getEmployeeDAO().loadEmployee(employeeAccessBean.getEmail());
            if (oldEmployee != null) {
                Employee newEmployee = oldEmployee.copy();

                Employee tempEmployee = new Employee(this.getLoggedEmployee().getEmail(), employeeAccessBean.getPassword());
                if (tempEmployee.hasDefaultPassword()) {
                    throw new InvalidInputException(InvalidInputException.InputType.DEFAULT_PASSWORD);
                }
                newEmployee.changePassword(employeeAccessBean.getPassword());

                this.startOperation();
                this.addStatement(this.getEmployeeDAO(), new Statement<>(List.of(newEmployee, oldEmployee), Statement.Type.UPDATE));
                this.endOperation();

                EmployeeBean newLoggedEmployee = new EmployeeBean(newEmployee);
                this.setLoggedEmployee(newLoggedEmployee);
                this.sendResult(ControllerEvent.SUCCESS_EMPLOYEE_UPDATED, newLoggedEmployee);

            } else throw new EntityNotFoundException(EntityException.Entity.EMPLOYEE);
        } else {
            throw new InvalidInputException(InvalidInputException.InputType.DEFAULT_PASSWORD);
        }
    }

    public void saveNewPersonalInfo(EmployeeBean employeeBean) throws EntityNotFoundException, PersistenceException {
        Employee oldEmployee = this.getEmployeeDAO().loadEmployee(this.getLoggedEmployee().getEmail());
        if (oldEmployee != null) {
            Employee newEmployee = oldEmployee.copy();

            String name = employeeBean.getName();
            if (name != null) newEmployee.changeName(name);

            String email = employeeBean.getEmail();
            if (email != null) newEmployee.changeEmail(email);

            String phone = employeeBean.getPhone();
            if (phone != null) newEmployee.changePhone(phone);

            this.startOperation();
            this.addStatement(this.getEmployeeDAO(), new Statement<>(List.of(newEmployee, oldEmployee), Statement.Type.UPDATE));
            this.endOperation();

            EmployeeBean newLoggedEmployee = new EmployeeBean(newEmployee);
            this.setLoggedEmployee(newLoggedEmployee);
            this.sendResult(ControllerEvent.SUCCESS_EMPLOYEE_UPDATED, newLoggedEmployee);

        } else throw new EntityNotFoundException(EntityException.Entity.EMPLOYEE);
    }
}
