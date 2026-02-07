package it.orderflow.view;

import it.orderflow.beans.ProductBean;
import it.orderflow.beans.ProductInStockBean;
import it.orderflow.exceptions.InvalidInputException;

import java.util.List;

public interface ManageProductsView extends BasicView {

    void displayManageProducts();

    void displayAddSupplierProduct(List<String> attributesIds);

    void displayAddProductInStock(List<String> attributesIds);

    ProductBean getProductBean() throws InvalidInputException;

    ProductInStockBean getProductInStockBean() throws InvalidInputException;
}
