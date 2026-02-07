package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.ProductInStock;

import java.util.List;

public interface ProductInStockDAO extends TransactionControl<ProductInStock> {

    ProductInStock loadProductInStock(String code) throws DatabaseException;

    List<ProductInStock> loadByArticleName(String articleName) throws DatabaseException;

    List<ProductInStock> loadAll() throws DatabaseException;
}
