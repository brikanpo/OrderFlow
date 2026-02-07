package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.SupplierArticle;

import java.util.List;

public interface SupplierArticleDAO extends TransactionControl<SupplierArticle> {

    SupplierArticle loadSupplierArticle(String name) throws DatabaseException;

    List<SupplierArticle> loadAll() throws DatabaseException;
}
