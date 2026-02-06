package it.OrderFlow.View;

import it.OrderFlow.Beans.EmployeeBean;
import it.OrderFlow.Exceptions.InvalidInputException;

import java.util.List;

public interface ManageEmployeesView extends BasicView {

    void displayManageEmployees();

    void displayAddEmployee();

    void displayChangeRole(List<EmployeeBean> employeesBeans);

    EmployeeBean getEmployeeBean() throws InvalidInputException;
}
