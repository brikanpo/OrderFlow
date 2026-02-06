package it.OrderFlow.View;

import it.OrderFlow.Beans.ProductBean;
import it.OrderFlow.Beans.ProductInStockBean;
import it.OrderFlow.Exceptions.InvalidInputException;

import java.util.List;

public interface ManageProductsView extends BasicView {

    void displayManageProducts();

    void displayAddSupplierProduct(List<String> attributesIds);

    void displayAddProductInStock(List<String> attributesIds);

    ProductBean getProductBean() throws InvalidInputException;

    ProductInStockBean getProductInStockBean() throws InvalidInputException;
}
