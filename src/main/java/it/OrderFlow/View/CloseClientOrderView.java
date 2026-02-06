package it.OrderFlow.View;

import it.OrderFlow.Beans.ClientOrderBean;

public interface CloseClientOrderView extends BasicView {

    void displayClientOrderDetails(ClientOrderBean clientOrder);
}
