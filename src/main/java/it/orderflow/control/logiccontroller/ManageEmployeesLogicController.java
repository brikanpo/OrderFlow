package it.orderflow.control.logiccontroller;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.Statement;
import it.orderflow.control.TransactionSafeController;
import it.orderflow.dao.EmployeeDAO;
import it.orderflow.exceptions.*;
import it.orderflow.model.Employee;
import it.orderflow.services.EmailSenderService;

import java.util.ArrayList;
import java.util.List;

public class ManageEmployeesLogicController extends TransactionSafeController {

    private final EmployeeDAO employeeDAO;

    public ManageEmployeesLogicController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return this.employeeDAO;
    }

    public List<EmployeeBean> getEmployeesList() throws PersistenceException {
        List<Employee> employees = this.getEmployeeDAO().loadAll();

        List<EmployeeBean> employeeBeans = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeBean employeeBean = new EmployeeBean(employee);
            employeeBeans.add(employeeBean);
        }
        return employeeBeans;
    }

    public void saveNewEmployee(EmployeeBean employeeBean)
            throws AlreadyInUseException, EmailNotSentException, PersistenceException {
        Employee targetEmployee = this.getEmployeeDAO().loadEmployee(employeeBean.getEmail());
        if (targetEmployee == null) {
            Employee tempEmployee = new Employee(employeeBean.getEmail(), employeeBean.getEmail());
            tempEmployee.changeName(employeeBean.getName());
            tempEmployee.changeRole(employeeBean.getUserRole());

            this.startOperation();
            this.addStatement(this.getEmployeeDAO(), new Statement<>(List.of(tempEmployee), Statement.Type.SAVE));
            this.endOperation();

            EmailSenderService emailSender = new EmailSenderService();
            emailSender.sendEmailNotification(EmailSenderService.EmailType.NEW_EMPLOYEE,
                    employeeBean.getEmail(), employeeBean.getUserRole().toString());

        } else throw new AlreadyInUseException(EntityException.Entity.EMPLOYEE, AlreadyInUseException.Param.EMAIL);
    }

    public void changeEmployeeRole(EmployeeBean employeeBean)
            throws EmailNotSentException, EntityNotFoundException, PersistenceException {
        Employee oldEmployee = this.getEmployeeDAO().loadEmployee(employeeBean.getEmail());
        if (oldEmployee != null) {
            Employee newEmployee = oldEmployee.copy();
            newEmployee.changeRole(employeeBean.getUserRole());

            this.startOperation();
            this.addStatement(this.getEmployeeDAO(), new Statement<>(List.of(newEmployee, oldEmployee), Statement.Type.UPDATE));
            this.endOperation();

            EmailSenderService emailSender = new EmailSenderService();
            emailSender.sendEmailNotification(EmailSenderService.EmailType.CHANGE_ROLE,
                    employeeBean.getEmail(), employeeBean.getUserRole().toString());

        } else throw new EntityNotFoundException(EntityException.Entity.EMPLOYEE);
    }
}
