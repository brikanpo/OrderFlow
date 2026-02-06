package it.OrderFlow.DAO;

import it.OrderFlow.Model.Client;

import java.util.List;

public interface ClientDAO extends TransactionControl<Client> {

    Client loadClient(String email) throws Exception;

    List<Client> loadAll() throws Exception;
}
