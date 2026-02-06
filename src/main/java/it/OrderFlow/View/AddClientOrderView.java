package it.OrderFlow.View;

import it.OrderFlow.Beans.ClientOrderBean;
import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Beans.ProductWithQuantityBean;
import it.OrderFlow.Exceptions.InvalidInputException;

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
