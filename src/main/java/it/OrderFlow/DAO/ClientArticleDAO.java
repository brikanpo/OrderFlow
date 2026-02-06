package it.OrderFlow.DAO;

import it.OrderFlow.Model.ClientArticle;

import java.util.List;

public interface ClientArticleDAO extends TransactionControl<ClientArticle> {

    ClientArticle loadClientArticle(String name) throws Exception;

    List<ClientArticle> loadAll() throws Exception;
}
