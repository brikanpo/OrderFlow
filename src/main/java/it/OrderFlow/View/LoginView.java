package it.OrderFlow.View;

import it.OrderFlow.Beans.EmployeeAccessBean;
import it.OrderFlow.Exceptions.InvalidInputException;

public interface LoginView extends BasicView {

    void displayFirstLogin();

    void displayLogin();

    EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException;
}
