package it.orderflow.view;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.beans.EmployeeBean;
import it.orderflow.exceptions.InvalidInputException;

public interface ManagePersonalInfoView extends BasicView {

    void displayManagePersonalInfo(EmployeeBean employeeBean);

    void displayChangePersonalInfo(EmployeeBean employeeBean);

    void displayChangePassword(boolean hasDefaultPassword);

    EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException;

    EmployeeBean getEmployeeBean() throws InvalidInputException;
}
