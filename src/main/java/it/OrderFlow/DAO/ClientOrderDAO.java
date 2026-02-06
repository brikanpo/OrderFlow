package it.OrderFlow.DAO;

import it.OrderFlow.Model.ClientOrder;
import it.OrderFlow.Model.OrderState;

import java.util.List;
import java.util.UUID;

public interface ClientOrderDAO extends TransactionControl<ClientOrder> {

    ClientOrder loadClientOrder(UUID id) throws Exception;

    List<ClientOrder> loadByState(OrderState state) throws Exception;

    List<ClientOrder> loadByStateAndClientId(OrderState state, UUID clientId) throws Exception;

    List<ClientOrder> loadAll() throws Exception;
}
