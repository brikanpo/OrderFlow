package it.orderflow.control.viewcontroller;

import it.orderflow.beans.EmployeeAccessBean;
import it.orderflow.control.ControllerEvent;
import it.orderflow.control.ControllerResultObserver;
import it.orderflow.control.logiccontroller.LoginLogicController;
import it.orderflow.view.LoginView;
import it.orderflow.view.ViewEvent;

public class LoginViewController extends RootViewController implements ControllerResultObserver {

    private LoginView view;
    private LoginLogicController logicController;

    public LoginViewController(LoginView view, LoginLogicController logicController) {
        this.setView(view);
        this.setLogicController(logicController);
    }

    public LoginView getView() {
        return this.view;
    }

    public void setView(LoginView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public LoginLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(LoginLogicController logicController) {
        this.logicController = logicController;
        this.logicController.addObserver(this);
    }

    @Override
    public void start() {
        try {
            if (this.getLogicController().isFirstAccess()) {
                this.getView().displayFirstLogin();
            } else {
                this.getView().displayLogin();
            }
        } catch (Exception e) {
            this.getView().displayErrorMessage(e);
        }
    }

    @Override
    public void onEvent(ViewEvent event) {
        if (event == ViewEvent.LOGIN) {
            try {
                EmployeeAccessBean employeeAccessBean = this.getView().getEmployeeAccessBean();

                this.getLogicController().authenticate(employeeAccessBean);
            } catch (Exception e) {
                this.getView().displayErrorMessage(e);
            }
        } else if (event == ViewEvent.EXIT) {
            this.getView().close();
            this.notifyObservers(ControllerEvent.EXIT);
        } else throw new UnsupportedOperationException();
    }

    @Override
    public <T> void onResult(ControllerEvent event, T result) {
        if (event == ControllerEvent.PASSWORD_CHANGE_REQUIRED) {
            this.sendResult(ControllerEvent.PASSWORD_CHANGE_REQUIRED, result);
        } else if (event == ControllerEvent.SUCCESS_LOGIN) {
            this.sendResult(ControllerEvent.SUCCESS_LOGIN, result);
        } else throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(ControllerEvent event) {
        throw new UnsupportedOperationException();
    }
}
