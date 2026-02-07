package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.ProductInStock;

import java.util.List;

public interface ProductInStockDAO extends TransactionControl<ProductInStock> {

    ProductInStock loadProductInStock(String code) throws PersistenceException;

    List<ProductInStock> loadByArticleName(String articleName) throws PersistenceException;

    List<ProductInStock> loadAll() throws PersistenceException;
}
