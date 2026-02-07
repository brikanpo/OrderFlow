package it.orderflow.view;

import it.orderflow.beans.ClientOrderBean;

public interface CloseClientOrderView extends BasicView {

    void displayClientOrderDetails(ClientOrderBean clientOrder);
}
