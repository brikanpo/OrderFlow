package it.orderflow.view.selectionviews;

import it.orderflow.beans.ClientBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.BasicView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public interface ClientSelectionView extends BasicView {

    void displayClients(List<ClientBean> clients, ViewEvent onBack);

    ClientBean selectedClient() throws InvalidInputException;
}
