package it.orderflow.view;

import it.orderflow.beans.ClientBean;
import it.orderflow.exceptions.InvalidInputException;

public interface ManageClientsView extends BasicView {

    void displayManageClients();

    void displayAddClient();

    void displayChangeClientInfo(ClientBean clientBean);

    ClientBean getClientBean() throws InvalidInputException;
}
