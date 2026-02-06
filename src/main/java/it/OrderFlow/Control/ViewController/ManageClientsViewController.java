package it.OrderFlow.Control.ViewController;

import it.OrderFlow.Beans.ClientBean;
import it.OrderFlow.Control.ControllerEvent;
import it.OrderFlow.Control.LogicController.ManageClientsLogicController;
import it.OrderFlow.View.ManageClientsView;
import it.OrderFlow.View.SelectionComponents.ClientSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class ManageClientsViewController extends RootViewController {

    private ManageClientsView view;
    private ClientSelectionView clientSelectionView;
    private ManageClientsLogicController logicController;

    public ManageClientsViewController(ManageClientsView view, ClientSelectionView clientSelectionView,
                                       ManageClientsLogicController logicController) {
        this.setView(view);
        this.setClientSelectionView(clientSelectionView);
        this.setLogicController(logicController);
    }

    public ManageClientsView getView() {
        return this.view;
    }

    public void setView(ManageClientsView view) {
        this.view = view;
        this.view.addObserver(this);
    }

    public ClientSelectionView getClientSelectionView() {
        return this.clientSelectionView;
    }

    public void setClientSelectionView(ClientSelectionView clientSelectionView) {
        this.clientSelectionView = clientSelectionView;
        this.clientSelectionView.addObserver(this);
    }

    public ManageClientsLogicController getLogicController() {
        return this.logicController;
    }

    public void setLogicController(ManageClientsLogicController logicController) {
        this.logicController = logicController;
    }

    public void addClient() {
        this.getView().displayAddClient();
    }

    @Override
    public void start() {
        this.getView().displayManageClients();
    }

    @Override
    public void onEvent(ViewEvent viewEvent) {
        switch (viewEvent) {
            case ADD_CLIENT -> this.addClient();
            case SAVE_CLIENT -> {
                try {
                    ClientBean clientBean = this.getView().getClientBean();

                    this.getLogicController().saveNewClient(clientBean);

                    this.getView().displaySuccessMessage("Operation add client successful");
                    this.start();
                } catch (Exception e) {
                    this.getView().displayErrorMessage(e);
                }
            }
            case CHANGE_CLIENT_INFO -> {
                try {
                    List<ClientBean> clientBeans = this.getLogicController().getClientsList();

                    this.getClientSelectionView().displayClients(clientBeans, ViewEvent.BACK);
                } catch (Exception e) {
                    this.getClientSelectionView().displayErrorMessage(e);
                }
            }
            case SELECTED_CLIENT -> {
                try {
                    ClientBean clientBean = this.getClientSelectionView().selectedClient();

                    this.getLogicController().addSelectedClient(clientBean);

                    this.getView().displayChangeClientInfo(clientBean);
                } catch (Exception e) {
                    this.getClientSelectionView().displayErrorMessage(e);
                }
            }
            case SAVE_CLIENT_INFO_CHANGE -> {
                try {
                    ClientBean clientBean = this.getView().getClientBean();

                    this.getLogicController().changeClientInfo(clientBean);

                    this.getView().displaySuccessMessage("Operation change client info was successful");
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
        }
    }
}
