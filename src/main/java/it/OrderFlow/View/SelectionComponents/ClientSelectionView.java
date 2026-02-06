package it.OrderFlow.View.SelectionComponents;

import it.OrderFlow.Beans.ClientBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.BasicView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public interface ClientSelectionView extends BasicView {

    void displayClients(List<ClientBean> clients, ViewEvent onBack);

    ClientBean selectedClient() throws InvalidInputException;
}
