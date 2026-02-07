package it.orderflow.control.viewcontroller;

import it.orderflow.control.ControllerEvent;
import it.orderflow.control.logiccontroller.ManageOrdersLogicController;
import it.orderflow.view.ManageOrdersView;
import it.orderflow.view.ViewEvent;

public class ManageOrdersViewController extends RootViewController {

    private ManageOrdersView view;
    private ManageOrdersLogicController logicController;

    public ManageOrdersViewController(ManageOrdersView view, ManageOrdersLogicController logicController) {
        super();
        this.setView(view);
        this.setLogicController(logicController);
    }

    public ManageOrdersView getView() {
        return this.view;
    }

    public void setView(ManageOrdersView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public ManageOrdersLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManageOrdersLogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void start() {
        this.getView().displayManageOrders();
    }

    @Override
    public void onEvent(ViewEvent viewEvent) {
        switch (viewEvent) {
            case MANAGE_CLIENTS_ORDERS -> this.getView().displayManageClientOrders();
            case ADD_CLIENT_ORDER -> this.notifyObservers(ControllerEvent.ADD_CLIENT_ORDER);
            case MANAGE_SUPPLIERS_ORDERS -> this.getView().displayManageSuppliersOrders();
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
