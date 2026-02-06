package it.OrderFlow.View.CLI;

import it.OrderFlow.Beans.EmployeeAccessBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.LoginView;
import it.OrderFlow.View.ViewEvent;

public class CLILoginView extends CLIRootView implements LoginView {

    private boolean firstLogin;

    private String email;
    private String password;

    public CLILoginView() {
        super();
    }

    private void insertLoginData() {
        System.out.print("Insert your email: ");
        this.email = scanner.nextLine();
        System.out.print("Insert your password: ");
        this.password = scanner.nextLine();
    }

    @Override
    public void displayFirstLogin() {
        this.firstLogin = true;
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Welcome to OrderFlow");
            System.out.println("You are the first user. Select Register to register yourself as a Manager");
            System.out.println("What do you want to do?");
            System.out.println("1) Register");
            System.out.println("2) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> {
                    this.insertLoginData();
                    this.notifyObservers(ViewEvent.LOGIN);
                }
                case "2" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public void displayLogin() {
        this.firstLogin = false;
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Welcome to OrderFlow");
            System.out.println("What do you want to do?");
            System.out.println("1) Login");
            System.out.println("2) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> {
                    this.insertLoginData();
                    this.notifyObservers(ViewEvent.LOGIN);
                }
                case "2" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException {
        EmployeeAccessBean employeeAccessBean = new EmployeeAccessBean();
        employeeAccessBean.setEmail(email);
        employeeAccessBean.setPassword(password);
        return employeeAccessBean;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        if (this.firstLogin) {
            this.displayFirstLogin();
        } else {
            this.displayLogin();
        }
    }
}
