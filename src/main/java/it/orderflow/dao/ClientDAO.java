package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.Client;

import java.util.List;

public interface ClientDAO extends TransactionControl<Client> {

    Client loadClient(String email) throws PersistenceException;

    List<Client> loadAll() throws PersistenceException;
}
