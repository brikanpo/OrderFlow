package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.Product;

import java.util.List;

public interface ProductDAO extends TransactionControl<Product> {

    Product loadProduct(String code) throws PersistenceException;

    List<Product> loadAll() throws PersistenceException;
}
