package it.OrderFlow.DAO.Cache;

import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.ClientDAO;
import it.OrderFlow.Exceptions.CacheIntegrityException;
import it.OrderFlow.Model.Client;

import java.util.List;
import java.util.UUID;

public class CacheClientDAO extends CacheGeneralDAO<Client> implements ClientDAO {

    private UUID getClientId(Client client) {
        return client.getId();
    }

    private String getClientEmail(Client client) {
        return client.getEmail();
    }

    private Client copy(Client client) {
        return client.clone();
    }

    private Client findClient(UUID id) {
        return this.findFromCache(id, this::getClientId);
    }

    private void saveNewClient(Client client) {
        this.saveNewEntity(client, this::loadClient, this::getClientEmail, this::copy);
    }

    private void updateClient(Client client) {
        this.updateEntity(client, this::findClient, this::getClientId);
    }

    private void deleteClient(Client client) {
        this.deleteEntity(client, this::findClient, this::getClientId);
    }

    @Override
    public Client loadClient(String email) {
        return this.findFromCache(email, this::getClientEmail);
    }

    @Override
    public List<Client> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<Client>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewClient, this::updateClient, this::deleteClient);
    }

    @Override
    public void keepIntegrity(List<Statement<Client>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewClient, this::updateClient, this::deleteClient);
    }
}
