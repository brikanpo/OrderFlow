package it.orderflow.view.cli;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.model.UserRole;
import it.orderflow.view.ManageEmployeesView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public class CLIManageEmployeesView extends CLIRootView implements ManageEmployeesView {

    private String name;
    private String email;
    private String role;
    private String index;
    private List<EmployeeBean> employees;

    public CLIManageEmployeesView() {
        super();
    }

    @Override
    public void displayManageEmployees() {
        boolean chosenAnOption = false;
        while (!chosenAnOption) {
            this.printTitle("Manage employees");
            System.out.println("What do you want to do?");
            System.out.println("1) Add employee");
            System.out.println("2) Change employee role");
            System.out.println("3) Home");
            System.out.println("4) Logout");
            System.out.println("5) Exit");
            System.out.print("Type the number of the option chosen: ");
            switch (this.scanner.nextLine()) {
                case "1" -> this.notifyObservers(ViewEvent.ADD_EMPLOYEE);
                case "2" -> this.notifyObservers(ViewEvent.CHANGE_EMPLOYEE_ROLE);
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
    public void displayAddEmployee() {
        this.printTitle("Add employee");
        System.out.print("Insert employee name: ");
        this.name = scanner.nextLine();
        System.out.print("Insert employee email: ");
        this.email = scanner.nextLine();
        System.out.println("Employees roles are manager(m), representative(r), warehouse worker(w) and delivery worker(d)");
        System.out.print("Type the letter for the employee role chosen: ");
        this.role = scanner.nextLine();
        this.confirmOperation(ViewEvent.SAVE_EMPLOYEE, ViewEvent.BACK);
    }

    @Override
    public void displayChangeRole(List<EmployeeBean> employees) {
        this.employees = employees;
        this.printTitle("Change employee role");
        System.out.println("Which employee's role do you want to change?");
        System.out.printf("%-2s) | %-25s | %-35s | %-16s |%n", "N°", "Name", "Email", "Role");
        for (int i = 0; i < employees.size(); i++) {
            EmployeeBean eb = employees.get(i);
            System.out.printf("%-2d) | %-25s | %-35s | %-16s |%n",
                    (i + 1), eb.getName(), eb.getEmail(), eb.getUserRole().toString());
        }
        System.out.print("Type the number of the employee chosen: ");
        this.index = this.scanner.nextLine();
        System.out.println("Employees roles are manager(m), representative(r), warehouse worker(w) and delivery worker(d)");
        System.out.print("Type the letter for the employee role chosen: ");
        this.role = scanner.nextLine();
        this.confirmOperation(ViewEvent.SAVE_EMPLOYEE_ROLE_CHANGE, ViewEvent.BACK);
    }

    @Override
    public EmployeeBean getEmployeeBean() throws InvalidInputException {
        EmployeeBean employeeBean = new EmployeeBean();

        if (this.index == null) {
            employeeBean.setName(this.name);
            employeeBean.setEmail(this.email);
        } else {
            try {
                employeeBean = this.employees.get(Integer.parseInt(this.index) - 1);
            } catch (Exception e) {
                throw new InvalidInputException(InvalidInputException.InputType.SELECTION);
            }
        }

        UserRole userRoleString = switch (this.role) {
            case "m" -> UserRole.MANAGER;
            case "r" -> UserRole.REPRESENTATIVE;
            case "w" -> UserRole.WAREHOUSE_WORKER;
            case "d" -> UserRole.DELIVERY_WORKER;
            default -> null;
        };

        employeeBean.setUserRole(userRoleString);

        this.index = null;

        return employeeBean;
    }

    @Override
    public void displayErrorMessage(Exception e) {
        super.displayErrorMessage(e);
        this.displayManageEmployees();
    }
}
