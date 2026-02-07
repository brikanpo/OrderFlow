package it.orderflow.control.viewcontroller;

import it.orderflow.beans.EmployeeBean;
import it.orderflow.control.ControllerEvent;
import it.orderflow.model.UserRole;
import it.orderflow.view.HomeView;
import it.orderflow.view.ViewEvent;

public class HomeViewController extends RootViewController {

    private HomeView view;
    private EmployeeBean loggedEmployee;

    public HomeViewController(HomeView view, EmployeeBean loggedEmployee) {
        this.setView(view);
        this.setLoggedEmployee(loggedEmployee);
    }

    public HomeView getView() {
        return this.view;
    }

    public void setView(HomeView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public EmployeeBean getLoggedEmployee() {
        return this.loggedEmployee;
    }

    public void setLoggedEmployee(EmployeeBean loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
    }

    @Override
    public void start() {
        switch (this.getLoggedEmployee().getUserRole()) {
            case UserRole.MANAGER -> this.getView().displayManagerHome();
            case UserRole.REPRESENTATIVE -> this.getView().displayRepresentativeHome();
            case UserRole.WAREHOUSE_WORKER -> this.getView().displayWarehouseWorkerHome();
            case UserRole.DELIVERY_WORKER -> this.getView().displayDeliveryWorkerHome();
        }
    }

    @Override
    public void onEvent(ViewEvent event) {
        switch (event) {
            case ADD_CLIENT_ORDER -> this.notifyObservers(ControllerEvent.ADD_CLIENT_ORDER);
            case MANAGE_ARTICLES -> this.notifyObservers(ControllerEvent.MANAGE_ARTICLES);
            case MANAGE_CLIENTS -> this.notifyObservers(ControllerEvent.MANAGE_CLIENTS);
            case MANAGE_CLIENTS_ORDERS -> this.notifyObservers(ControllerEvent.MANAGE_CLIENTS_ORDERS);
            case MANAGE_EMPLOYEES -> this.notifyObservers(ControllerEvent.MANAGE_EMPLOYEES);
            case MANAGE_ORDERS -> this.notifyObservers(ControllerEvent.MANAGE_ORDERS);
            case MANAGE_PRODUCTS -> this.notifyObservers(ControllerEvent.MANAGE_PRODUCTS);
            case MANAGE_PERSONAL_INFO -> this.notifyObservers(ControllerEvent.MANAGE_PERSONAL_INFO);
            case MANAGE_SUPPLIERS -> this.notifyObservers(ControllerEvent.MANAGE_SUPPLIERS);
            case LOGOUT -> this.notifyObservers(ControllerEvent.LOGOUT);
            case EXIT -> {
                this.getView().close();
                this.notifyObservers(ControllerEvent.EXIT);
            }
            default -> throw new UnsupportedOperationException();
        }
    }
}
