package it.orderflow.view;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.exceptions.InvalidInputException;

public interface LoginView extends BasicView {

    void displayFirstLogin();

    void displayLogin();

    EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException;
}
