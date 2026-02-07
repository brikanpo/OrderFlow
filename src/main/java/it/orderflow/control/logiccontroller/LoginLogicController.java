package it.orderflow.control.logiccontroller;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.ControllerEvent;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.EmployeeDAO;
import it.orderflow.exceptions.FailedAuthenticationException;
import it.orderflow.model.Employee;
import it.orderflow.model.UserRole;

import java.util.List;

public class LoginLogicController extends TransactionSafeController {

    private final EmployeeDAO employeeDAO;
    private boolean firstAccess = false;

    public LoginLogicController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return this.employeeDAO;
    }

    public boolean isFirstAccess() throws Exception {
        this.firstAccess = this.getEmployeeDAO().isEmpty();
        return this.firstAccess;
    }

    public void authenticate(EmployeeAccessBean employeeAccessBean) throws Exception {
        Employee tempEmployee = new Employee(employeeAccessBean.getEmail(), employeeAccessBean.getPassword());
        if (this.firstAccess) {
            tempEmployee.changeRole(UserRole.MANAGER);

            this.startOperation();
            this.addStatement(this.getEmployeeDAO(), new Statement<>(List.of(tempEmployee), Statement.Type.SAVE));
            this.endOperation();

            if (tempEmployee.hasDefaultPassword()) {
                this.sendResult(ControllerEvent.PASSWORD_CHANGE_REQUIRED, new EmployeeBean(tempEmployee));
            } else this.sendResult(ControllerEvent.SUCCESS_LOGIN, new EmployeeBean(tempEmployee));

        } else {
            Employee targetEmployee = this.getEmployeeDAO().loadEmployee(employeeAccessBean.getEmail());

            if (targetEmployee.authenticate(tempEmployee)) {
                if (targetEmployee.hasDefaultPassword()) {
                    this.sendResult(ControllerEvent.PASSWORD_CHANGE_REQUIRED, new EmployeeBean(targetEmployee));
                } else this.sendResult(ControllerEvent.SUCCESS_LOGIN, new EmployeeBean(targetEmployee));
            } else throw new FailedAuthenticationException();
        }
    }
}
