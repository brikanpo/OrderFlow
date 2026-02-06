package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.EmployeeAccessBean;
import it.OrderFlow.Beans.EmployeeBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.ControllerResultObserver;
import it.OrderFlow.Control.LogicController.ManagePersonalInfoLogicController;
import it.OrderFlow.View.ManagePersonalInfoView;
import it.OrderFlow.View.ViewEvent;

public class ManagePersonalInfoViewController extends RootViewController implements ControllerResultObserver {

    private ManagePersonalInfoView view;
    private ManagePersonalInfoLogicController logicController;

    public ManagePersonalInfoViewController(ManagePersonalInfoView view, ManagePersonalInfoLogicController logicController) {
        this.setView(view);
        this.setLogicController(logicController);
    }

    public ManagePersonalInfoView getView() {
        return this.view;
    }

    public void setView(ManagePersonalInfoView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public ManagePersonalInfoLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManagePersonalInfoLogicController logicController) {
        this.logicController = logicController;
        this.logicController.addObserver(this);
    }

    public boolean checkDefaultPassword() {
        try {
            return this.getLogicController().hasDefaultPassword();
        } catch (Exception e) {
            return false;
        }
    }

    public void changePassword(boolean hasDefaultPassword) {
        this.getView().displayChangePassword(hasDefaultPassword);

    }

    @Override
    public void start() {
        EmployeeBean loggedEmployee = this.getLogicController().getLoggedEmployee();
        this.getView().displayManagePersonalInfo(loggedEmployee);
    }

    @Override
    public void onEvent(ViewEvent event) {
        switch (event) {
            case CHANGE_PERSONAL_INFO -> {
                EmployeeBean loggedEmployee = this.getLogicController().getLoggedEmployee();
                this.getView().displayChangePersonalInfo(loggedEmployee);
            }
            case SAVE_PERSONAL_INFO -> {
                try {
                    EmployeeBean employeeBean = this.getView().getEmployeeBean();

                    this.getLogicController().saveNewPersonalInfo(employeeBean);

                    this.getView().displaySuccessMessage("Operation change personal info was successful");
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case CHANGE_PASSWORD -> this.changePassword(false);
            case SAVE_PASSWORD -> {
                try {
                    EmployeeBean loggedEmployee = this.getLogicController().getLoggedEmployee();

                    EmployeeAccessBean employeeAccessBean = this.getView().getEmployeeAccessBean();
                    employeeAccessBean.setEmail(loggedEmployee.getEmail());

                    this.getLogicController().saveNewPassword(employeeAccessBean);

                    this.getView().displaySuccessMessage("Operation change password was successful");
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
        }
    }

    @Override
    public void onEvent(ControllerEvent event) {

    }

    @Override
    public <T> void onResult(ControllerEvent event, T result) {
        if (event == ControllerEvent.SUCCESS_EMPLOYEE_UPDATED) {
            this.sendResult(ControllerEvent.SUCCESS_EMPLOYEE_UPDATED, result);
            this.start();
        }
    }
}
