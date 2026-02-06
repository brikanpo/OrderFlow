package it.OrderFlow.DAO;

import it.OrderFlow.Model.Product;

import java.util.List;

public interface ProductDAO extends TransactionControl<Product> {

    Product loadProduct(String code) throws Exception;

    List<Product> loadAll() throws Exception;
}
