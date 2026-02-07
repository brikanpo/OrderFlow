package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.ClientOrder;
import it.orderflow.model.OrderState;

import java.util.List;
import java.util.UUID;

public interface ClientOrderDAO extends TransactionControl<ClientOrder> {

    ClientOrder loadClientOrder(UUID id) throws PersistenceException;

    List<ClientOrder> loadByState(OrderState state) throws PersistenceException;

    List<ClientOrder> loadByStateAndClientId(OrderState state, UUID clientId) throws PersistenceException;

    List<ClientOrder> loadAll() throws PersistenceException;
}
