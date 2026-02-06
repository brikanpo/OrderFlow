package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.EmployeeAccessBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.ControllerResultObserver;
import it.OrderFlow.Control.LogicController.LoginLogicController;
import it.OrderFlow.View.LoginView;
import it.OrderFlow.View.ViewEvent;

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
        switch (event) {
            case LOGIN -> {
                try {
                    EmployeeAccessBean employeeAccessBean = this.getView().getEmployeeAccessBean();

                    this.getLogicController().authenticate(employeeAccessBean);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case EXIT -> {
                this.getView().close();
                this.notifyObservers(ControllerEvent.EXIT);
            }
        }
    }

    @Override
    public <T> void onResult(ControllerEvent event, T result) {
        switch (event) {
            case PASSWORD_CHANGE_REQUIRED -> this.sendResult(ControllerEvent.PASSWORD_CHANGE_REQUIRED, result);
            case SUCCESS_LOGIN -> this.sendResult(ControllerEvent.SUCCESS_LOGIN, result);
        }
    }

    @Override
    public void onEvent(ControllerEvent event) {

    }
}
