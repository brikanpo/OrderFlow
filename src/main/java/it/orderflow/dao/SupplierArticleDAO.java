package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.SupplierArticle;

import java.util.List;

public interface SupplierArticleDAO extends TransactionControl<SupplierArticle> {

    SupplierArticle loadSupplierArticle(String name) throws PersistenceException;

    List<SupplierArticle> loadAll() throws PersistenceException;
}
