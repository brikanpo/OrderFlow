package it.OrderFlow.DAO.Cache;

import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.ClientOrderDAO;
import it.OrderFlow.Exceptions.CacheIntegrityException;
import it.OrderFlow.Model.ClientOrder;
import it.OrderFlow.Model.OrderState;

import java.util.List;
import java.util.UUID;

public class CacheClientOrderDAO extends CacheGeneralDAO<ClientOrder> implements ClientOrderDAO {

    private UUID getClientOrderId(ClientOrder clientOrder) {
        return clientOrder.getId();
    }

    private UUID getClientOrderClientId(ClientOrder clientOrder) {
        return clientOrder.getClient().getId();
    }

    private OrderState getClientOrderState(ClientOrder clientOrder) {
        return clientOrder.getState();
    }

    private ClientOrder copy(ClientOrder clientOrder) {
        return clientOrder.clone();
    }

    private void saveNewClosedClientOrder(ClientOrder clientOrder) {
        this.saveNewEntity(clientOrder, this::loadClientOrder, this::getClientOrderId, this::copy);
    }

    private void updateClosedClientOrder(ClientOrder clientOrder) {
        this.updateEntity(clientOrder, this::loadClientOrder, this::getClientOrderId);
    }

    private void deleteClosedClientOrder(ClientOrder clientOrder) {
        this.deleteEntity(clientOrder, this::loadClientOrder, this::getClientOrderId);
    }

    @Override
    public ClientOrder loadClientOrder(UUID id) {
        return this.findFromCache(id, this::getClientOrderId);
    }

    @Override
    public List<ClientOrder> loadByState(OrderState state) {
        return this.findMatchesFromCache(state, this::getClientOrderState);
    }

    @Override
    public List<ClientOrder> loadByStateAndClientId(OrderState state, UUID clientId) {
        return this.findMatchesFromCache(state, this::getClientOrderState, clientId, this::getClientOrderClientId);
    }

    @Override
    public List<ClientOrder> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<ClientOrder>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewClosedClientOrder, this::updateClosedClientOrder,
                this::deleteClosedClientOrder);
    }

    @Override
    public void keepIntegrity(List<Statement<ClientOrder>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewClosedClientOrder, this::updateClosedClientOrder,
                this::deleteClosedClientOrder);
    }
}
