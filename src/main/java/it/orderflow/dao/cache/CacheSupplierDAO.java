package it.orderflow.dao.cache;

import it.orderflow.control.Statement;
import it.orderflow.dao.SupplierDAO;
import it.orderflow.exceptions.CacheIntegrityException;
import it.orderflow.model.Supplier;

import java.util.List;
import java.util.UUID;

public class CacheSupplierDAO extends CacheGeneralDAO<Supplier> implements SupplierDAO {

    private UUID getSupplierId(Supplier supplier) {
        return supplier.getId();
    }

    private String getSupplierEmail(Supplier supplier) {
        return supplier.getEmail();
    }

    private Supplier copy(Supplier supplier) {
        return supplier.clone();
    }

    private Supplier findSupplier(UUID id) {
        return this.findFromCache(id, this::getSupplierId);
    }

    private void saveNewSupplier(Supplier supplier) {
        this.saveNewEntity(supplier, this::loadSupplier, this::getSupplierEmail, this::copy);
    }

    private void updateSupplier(Supplier supplier) {
        this.updateEntity(supplier, this::findSupplier, this::getSupplierId);
    }

    private void deleteSupplier(Supplier supplier) {
        this.deleteEntity(supplier, this::findSupplier, this::getSupplierId);
    }

    @Override
    public Supplier loadSupplier(String email) {
        return this.findFromCache(email, this::getSupplierEmail);
    }

    @Override
    public List<Supplier> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<Supplier>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewSupplier, this::updateSupplier, this::deleteSupplier);
    }

    @Override
    public void keepIntegrity(List<Statement<Supplier>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewSupplier, this::updateSupplier, this::deleteSupplier);
    }
}
