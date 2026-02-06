package it.OrderFlow.View.SelectionComponents;

import it.OrderFlow.Beans.ClientOrderBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.BasicView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public interface ClientOrderSelectionView extends BasicView {

    void displayClientsOrders(List<ClientOrderBean> clientsOrders, ViewEvent onBack);

    ClientOrderBean selectedClientOrder() throws InvalidInputException;
}
