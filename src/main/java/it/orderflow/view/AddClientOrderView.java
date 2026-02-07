package it.orderflow.view;

import it.orderflow.beans.ClientOrderBean;
import it.orderflow.beans.ProductBean;
import it.orderflow.beans.ProductWithQuantityBean;
import it.orderflow.exceptions.InvalidInputException;

import java.util.List;

public interface AddClientOrderView extends BasicView {

    void displayInsertionOptions();

    void displayQuantitySelection(List<ProductBean> products);

    void displayUnavailableProducts(List<ProductWithQuantityBean> products);

    void displayClientOrderDetails(ClientOrderBean clientOrder);

    int selectedNumberOfPastOrders() throws InvalidInputException;

    List<ProductWithQuantityBean> selectedProductsQuantity() throws InvalidInputException;

    List<ProductWithQuantityBean> selectedProductsToBeRemoved() throws InvalidInputException;
}
