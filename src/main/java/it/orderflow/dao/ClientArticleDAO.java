package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.ClientArticle;

import java.util.List;

public interface ClientArticleDAO extends TransactionControl<ClientArticle> {

    ClientArticle loadClientArticle(String name) throws PersistenceException;

    List<ClientArticle> loadAll() throws PersistenceException;
}
