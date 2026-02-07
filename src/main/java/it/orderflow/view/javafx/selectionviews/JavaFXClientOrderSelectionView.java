package it.orderflow.view.javafx.selectionviews;

import it.orderflow.beans.ClientOrderBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.ViewEvent;
import it.orderflow.view.javafx.mycomponents.specifictableview.MyTableViewClientOrder;
import it.orderflow.view.selectionviews.ClientOrderSelectionView;

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
