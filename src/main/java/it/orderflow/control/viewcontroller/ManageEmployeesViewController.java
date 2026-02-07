package it.orderflow.control.viewcontroller;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.ControllerEvent;
import it.orderflow.control.logiccontroller.ManageEmployeesLogicController;
import it.orderflow.view.ManageEmployeesView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public class ManageEmployeesViewController extends RootViewController {

    private ManageEmployeesView view;
    private ManageEmployeesLogicController logicController;

    public ManageEmployeesViewController(ManageEmployeesView view, ManageEmployeesLogicController logicController) {
        this.setView(view);
        this.setLogicController(logicController);
    }

    public ManageEmployeesView getView() {
        return this.view;
    }

    public void setView(ManageEmployeesView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public ManageEmployeesLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManageEmployeesLogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void start() {
        this.getView().displayManageEmployees();
    }

    @Override
    public void onEvent(ViewEvent event) {
        switch (event) {
            case ADD_EMPLOYEE -> this.getView().displayAddEmployee();
            case SAVE_EMPLOYEE -> {
                try {
                    EmployeeBean employeeBean = this.getView().getEmployeeBean();

                    this.getLogicController().saveNewEmployee(employeeBean);

                    this.getView().displaySuccessMessage("Operation add employee successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case CHANGE_EMPLOYEE_ROLE -> {
                try {
                    List<EmployeeBean> employeeBeans = this.getLogicController().getEmployeesList();

                    this.getView().displayChangeRole(employeeBeans);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SAVE_EMPLOYEE_ROLE_CHANGE -> {
                try {
                    EmployeeBean employeeBean = this.getView().getEmployeeBean();

                    this.getLogicController().changeEmployeeRole(employeeBean);

                    this.getView().displaySuccessMessage("Operation change role successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case BACK -> this.start();
            case HOME -> this.notifyObservers(ControllerEvent.HOME);
            case LOGOUT -> this.notifyObservers(ControllerEvent.LOGOUT);
            case EXIT -> {
                this.getView().close();
                this.notifyObservers(ControllerEvent.EXIT);
            }
            default -> throw new UnsupportedOperationException();
        }
    }
}
