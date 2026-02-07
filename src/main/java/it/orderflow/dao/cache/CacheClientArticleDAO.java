package it.orderflow.dao.cache;

import it.orderflow.control.Statement;
import it.orderflow.dao.ClientArticleDAO;
import it.orderflow.exceptions.CacheIntegrityException;
import it.orderflow.model.ClientArticle;

import java.util.List;
import java.util.UUID;

public class CacheClientArticleDAO extends CacheGeneralDAO<ClientArticle> implements ClientArticleDAO {

    private UUID getClientArticleId(ClientArticle clientArticle) {
        return clientArticle.getId();
    }

    private String getClientArticleName(ClientArticle clientArticle) {
        return clientArticle.getName();
    }

    private ClientArticle copy(ClientArticle clientArticle) {
        return clientArticle.copy();
    }

    private void saveNewClientArticle(ClientArticle clientArticle) {
        this.saveNewEntity(clientArticle, this::loadClientArticle, this::getClientArticleName, this::copy);
    }

    private void updateClientArticle(ClientArticle clientArticle) {
        this.updateEntity(clientArticle, this::findClientArticle, this::getClientArticleId);
    }

    private void deleteClientArticle(ClientArticle clientArticle) {
        this.deleteEntity(clientArticle, this::findClientArticle, this::getClientArticleId);
    }

    private ClientArticle findClientArticle(UUID id) {
        return this.findFromCache(id, this::getClientArticleId);
    }

    @Override
    public ClientArticle loadClientArticle(String name) {
        return this.findFromCache(name, this::getClientArticleName);
    }

    @Override
    public List<ClientArticle> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<ClientArticle>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewClientArticle, this::updateClientArticle,
                this::deleteClientArticle);
    }

    @Override
    public void keepIntegrity(List<Statement<ClientArticle>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewClientArticle, this::updateClientArticle,
                this::deleteClientArticle);
    }
}
