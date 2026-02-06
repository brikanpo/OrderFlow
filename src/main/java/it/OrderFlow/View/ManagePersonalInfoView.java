package it.OrderFlow.View;

import it.OrderFlow.Beans.EmployeeAccessBean;
import it.OrderFlow.Beans.EmployeeBean;
import it.OrderFlow.Exceptions.InvalidInputException;

public interface ManagePersonalInfoView extends BasicView {

    void displayManagePersonalInfo(EmployeeBean employeeBean);

    void displayChangePersonalInfo(EmployeeBean employeeBean);

    void displayChangePassword(boolean hasDefaultPassword);

    EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException;

    EmployeeBean getEmployeeBean() throws InvalidInputException;
}
