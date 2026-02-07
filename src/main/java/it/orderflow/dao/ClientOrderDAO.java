package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.ClientOrder;
import it.orderflow.model.OrderState;

import java.util.List;
import java.util.UUID;

public interface ClientOrderDAO extends TransactionControl<ClientOrder> {

    ClientOrder loadClientOrder(UUID id) throws DatabaseException;

    List<ClientOrder> loadByState(OrderState state) throws DatabaseException;

    List<ClientOrder> loadByStateAndClientId(OrderState state, UUID clientId) throws DatabaseException;

    List<ClientOrder> loadAll() throws DatabaseException;
}
