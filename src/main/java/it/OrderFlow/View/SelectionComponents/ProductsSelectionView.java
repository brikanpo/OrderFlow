package it.OrderFlow.View.SelectionComponents;

import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Exceptions.InvalidInputException;
import it.OrderFlow.View.BasicView;
import it.OrderFlow.View.ViewEvent;

import java.util.List;

public interface ProductsSelectionView extends BasicView {

    void displayProducts(List<ProductBean> products, ViewEvent onBack);

    List<ProductBean> selectedProducts() throws InvalidInputException;
}
