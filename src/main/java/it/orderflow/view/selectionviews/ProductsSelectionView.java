package it.orderflow.view.selectionviews;

import it.orderflow.beans.ProductBean;
import it.orderflow.exceptions.InvalidInputException;
import it.orderflow.view.BasicView;
import it.orderflow.view.ViewEvent;

import java.util.List;

public interface ProductsSelectionView extends BasicView {

    void displayProducts(List<ProductBean> products, ViewEvent onBack);

    List<ProductBean> selectedProducts() throws InvalidInputException;
}
