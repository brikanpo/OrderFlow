package it.orderflow.view;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.exceptions.InvalidInputException;

import java.util.List;

public interface ManageEmployeesView extends BasicView {

    void displayManageEmployees();

    void displayAddEmployee();

    void displayChangeRole(List<EmployeeBean> employeesBeans);

    EmployeeBean getEmployeeBean() throws InvalidInputException;
}
