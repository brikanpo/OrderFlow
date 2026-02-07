package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.ClientArticle;

import java.util.List;

public interface ClientArticleDAO extends TransactionControl<ClientArticle> {

    ClientArticle loadClientArticle(String name) throws DatabaseException;

    List<ClientArticle> loadAll() throws DatabaseException;
}
