package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.Product;

import java.util.List;

public interface ProductDAO extends TransactionControl<Product> {

    Product loadProduct(String code) throws DatabaseException;

    List<Product> loadAll() throws DatabaseException;
}
