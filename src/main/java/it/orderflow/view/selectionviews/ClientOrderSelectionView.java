package it.orderflow.view.selectionviews;

import it.orderflow.beans.ClientOrderBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.BasicView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public interface ClientOrderSelectionView extends BasicView {

    void displayClientsOrders(List<ClientOrderBean> clientsOrders, ViewEvent onBack);

    ClientOrderBean selectedClientOrder() throws InvalidInputException;
}
