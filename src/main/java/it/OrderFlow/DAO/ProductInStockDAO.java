package it.OrderFlow.DAO;

import it.OrderFlow.Model.ProductInStock;

import java.util.List;

public interface ProductInStockDAO extends TransactionControl<ProductInStock> {

    ProductInStock loadProductInStock(String code) throws Exception;

    List<ProductInStock> loadByArticleName(String articleName) throws Exception;

    List<ProductInStock> loadAll() throws Exception;
}
