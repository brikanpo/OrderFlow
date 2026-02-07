package it.orderflow.view.cli;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.beans.EmployeeBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ManagePersonalInfoView;
import it.orderflow.view.ViewEvent;

public class CLIManagePersonalInfoView extends CLIRootView implements ManagePersonalInfoView {

    private String name;
    private String email;
    private String phone;
    private String password;
    private EmployeeBean employeeBean;

    @Override
    public void displayManagePersonalInfo(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage personal info");
            System.out.println("You are logged in as a " + employeeBean.getUserRole());
            System.out.println("These are your info");
            System.out.println("Name: " + employeeBean.getName());
            System.out.println("Email: " + employeeBean.getEmail());
            System.out.println("Phone: " + employeeBean.getPhone());
            System.out.println("What do you want to do?");
            System.out.println("1) Change personal info");
            System.out.println("2) Change password");
            System.out.println("3) Home");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.CHANGE_PERSONAL_INFO);
                case "2" -> this.notifyObservers(ViewEvent.CHANGE_PASSWORD);
                case "3" -> this.notifyObservers(ViewEvent.HOME);
                case "4" -> this.notifyObservers(ViewEvent.LOGOUT);
                case "5" -> this.notifyObservers(ViewEvent.EXIT);
                default -> {
                    continue;
                }
            }
            chosenAnOption = true;
        }
    }

    @Override
    public void displayChangePersonalInfo(EmployeeBean employeeBean) {
        this.printTitle("Change personal info");
        System.out.println("These are your info");
        System.out.println("Name: " + employeeBean.getName());
        System.out.println("Email: " + employeeBean.getEmail());
        System.out.println("Phone: " + employeeBean.getPhone());
        System.out.println("If you don't want to change something leave the field empty");

        System.out.print("Insert your new name: ");
        this.name = scanner.nextLine();
        if (this.name.isBlank()) this.name = employeeBean.getName();

        System.out.print("Insert your new email: ");
        this.email = scanner.nextLine();
        if (this.email.isBlank()) this.email = employeeBean.getEmail();

        System.out.print("Insert your new phone: ");
        this.phone = scanner.nextLine();
        if (this.phone.isBlank()) this.phone = employeeBean.getPhone();

        this.confirmOperation(ViewEvent.SAVE_PERSONAL_INFO, ViewEvent.BACK);
    }

    @Override
    public void displayChangePassword(boolean hasDefaultPassword) {
        this.printTitle("Change password");

        if (hasDefaultPassword) {
            do {
                System.out.println("Change the default password");
                System.out.print("Insert your new password: ");
                this.password = scanner.nextLine();
            } while (this.password.isBlank());
        } else {
            System.out.println("If you don't want to change the password leave the field empty");
        }

        System.out.print("Insert your new password: ");
        this.password = scanner.nextLine();

        if (this.password.isBlank()) this.notifyObservers(ViewEvent.BACK);
        else this.confirmOperation(ViewEvent.SAVE_PASSWORD, ViewEvent.BACK);
    }

    @Override
    public EmployeeAccessBean getEmployeeAccessBean() throws InvalidInputException {
        EmployeeAccessBean employeeAccessBean = new EmployeeAccessBean();
        employeeAccessBean.setPassword(this.password);
        return employeeAccessBean;
    }

    @Override
    public EmployeeBean getEmployeeBean() throws InvalidInputException {
        EmployeeBean tempEmployeeBean = new EmployeeBean();
        tempEmployeeBean.setName(this.name);
        tempEmployeeBean.setEmail(this.email);
        tempEmployeeBean.setPhone(this.phone);
        return tempEmployeeBean;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayManagePersonalInfo(this.employeeBean);
    }
}
