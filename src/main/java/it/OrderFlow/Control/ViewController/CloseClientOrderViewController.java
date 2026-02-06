package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.ClientOrderBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.LogicController.CloseClientOrderLogicController;
import it.OrderFlow.View.CloseClientOrderView;
import it.OrderFlow.View.SelectionComponents.ClientOrderSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class CloseClientOrderViewController extends RootViewController {

    private CloseClientOrderView view;
    private ClientOrderSelectionView clientOrderSelectionView;
    private CloseClientOrderLogicController logicController;

    public CloseClientOrderViewController(CloseClientOrderView view,
                                          ClientOrderSelectionView clientOrderSelectionView,
                                          CloseClientOrderLogicController logicController) {
        super();
        this.setView(view);
        this.setClientOrderSelectionView(clientOrderSelectionView);
        this.setLogicController(logicController);
    }

    public CloseClientOrderView getView() {
        return this.view;
    }

    public void setView(CloseClientOrderView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public ClientOrderSelectionView getClientOrderSelectionView() {
        return this.clientOrderSelectionView;
    }

    public void setClientOrderSelectionView(ClientOrderSelectionView clientOrderSelectionView) {
        this.clientOrderSelectionView = clientOrderSelectionView;
        this.clientOrderSelectionView.addObserver(this);
    }

    public CloseClientOrderLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(CloseClientOrderLogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void start() {
        try {
            List<ClientOrderBean> readyOrders = this.getLogicController().getReadyOrdersList();

            this.getClientOrderSelectionView().displayClientsOrders(readyOrders, ViewEvent.BACK);
        } catch (Exception e) {
            this.getClientOrderSelectionView().displayErrorMessage(e);
        }
    }

    @Override
    public void onEvent(ViewEvent viewEvent) {
        switch (viewEvent) {
            case SELECTED_CLIENT_ORDER -> {
                try {
                    ClientOrderBean readyOrder = this.getClientOrderSelectionView().selectedClientOrder();

                    this.getLogicController().addSelectedOrder(readyOrder);

                    this.getView().displayClientOrderDetails(readyOrder);
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case SAVE_CLOSE_CLIENT_ORDER -> {
                try {
                    this.getLogicController().closeClientOrder();

                    this.getView().displaySuccessMessage("Operation close client order successful");
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case HOME -> this.notifyObservers(ControllerEvent.HOME);
            case LOGOUT -> this.notifyObservers(ControllerEvent.LOGOUT);
            case EXIT -> {
                this.getView().close();
                this.notifyObservers(ControllerEvent.EXIT);
            }
        }
    }
}
