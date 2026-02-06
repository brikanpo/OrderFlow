package it.OrderFlow.View.JavaFX.SelectionViews;

import it.OrderFlow.Beans.ClientOrderBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.JavaFX.MyComponents.SpecificTableView.MyTableViewClientOrder;
import it.OrderFlow.View.SelectionComponents.ClientOrderSelectionView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public class JavaFXClientOrderSelectionView extends JavaFXAbstractSelectionView<ClientOrderBean, MyTableViewClientOrder>
        implements ClientOrderSelectionView {

    public JavaFXClientOrderSelectionView(String title, String message) {
        super(title, message);
    }

    @Override
    public void displayClientsOrders(List<ClientOrderBean> clientsOrders, ViewEvent onBack) {
        this.displayItems(true, clientsOrders, onBack);
    }

    @Override
    public ClientOrderBean selectedClientOrder() throws InvalidInputException {
        return this.selectedItem();
    }

    @Override
    protected MyTableViewClientOrder createTable(List<ClientOrderBean> items) {
        return new MyTableViewClientOrder(items);
    }

    @Override
    protected void onConfirmPressed(boolean singleSelection) {
        this.notifyObservers(ViewEvent.SELECTED_CLIENT_ORDER);
    }
}
