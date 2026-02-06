package it.OrderFlow.View;

import it.OrderFlow.Beans.ClientBean;
import it.OrderFlow.Exceptions.InvalidInputException;

public interface ManageClientsView extends BasicView {

    void displayManageClients();

    void displayAddClient();

    void displayChangeClientInfo(ClientBean clientBean);

    ClientBean getClientBean() throws InvalidInputException;
}
