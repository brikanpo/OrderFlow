package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.Client;

import java.util.List;

public interface ClientDAO extends TransactionControl<Client> {

    Client loadClient(String email) throws DatabaseException;

    List<Client> loadAll() throws DatabaseException;
}
