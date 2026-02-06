package it.OrderFlow.DAO.Cache;

import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.SupplierArticleDAO;
import it.OrderFlow.Exceptions.CacheIntegrityException;
import it.OrderFlow.Model.SupplierArticle;

import java.util.List;
import java.util.UUID;

public class CacheSupplierArticleDAO extends CacheGeneralDAO<SupplierArticle> implements SupplierArticleDAO {

    private UUID getSupplierArticleId(SupplierArticle supplierArticle) {
        return supplierArticle.getId();
    }

    private String getSupplierArticleName(SupplierArticle supplierArticle) {
        return supplierArticle.getName();
    }

    private SupplierArticle copy(SupplierArticle supplierArticle) {
        return supplierArticle.clone();
    }

    private void saveNewSupplierArticle(SupplierArticle clientArticle) {
        this.saveNewEntity(clientArticle, this::loadSupplierArticle, this::getSupplierArticleName, this::copy);
    }

    private void updateSupplierArticle(SupplierArticle clientArticle) {
        this.updateEntity(clientArticle, this::findSupplierArticle, this::getSupplierArticleId);
    }

    private void deleteSupplierArticle(SupplierArticle clientArticle) {
        this.deleteEntity(clientArticle, this::findSupplierArticle, this::getSupplierArticleId);
    }

    private SupplierArticle findSupplierArticle(UUID id) {
        return this.findFromCache(id, this::getSupplierArticleId);
    }

    @Override
    public SupplierArticle loadSupplierArticle(String name) {
        return this.findFromCache(name, this::getSupplierArticleName);
    }

    @Override
    public List<SupplierArticle> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<SupplierArticle>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewSupplierArticle, this::updateSupplierArticle,
                this::deleteSupplierArticle);
    }

    @Override
    public void keepIntegrity(List<Statement<SupplierArticle>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewSupplierArticle, this::updateSupplierArticle,
                this::deleteSupplierArticle);
    }
}
