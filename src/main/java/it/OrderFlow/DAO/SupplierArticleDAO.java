package it.OrderFlow.DAO;

import it.OrderFlow.Exceptions.DatabaseException;
import it.OrderFlow.Model.SupplierArticle;

import java.util.List;

public interface SupplierArticleDAO extends TransactionControl<SupplierArticle> {

    SupplierArticle loadSupplierArticle(String name) throws DatabaseException;

    List<SupplierArticle> loadAll() throws DatabaseException;
}
