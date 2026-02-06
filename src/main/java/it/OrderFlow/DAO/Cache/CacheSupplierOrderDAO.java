package it.OrderFlow.DAO.Cache;

import it.OrderFlow.Control.Statement;
import it.OrderFlow.DAO.SupplierOrderDAO;
import it.OrderFlow.Exceptions.CacheIntegrityException;
import it.OrderFlow.Model.OrderState;
import it.OrderFlow.Model.SupplierOrder;

import java.util.List;
import java.util.UUID;

public class CacheSupplierOrderDAO extends CacheGeneralDAO<SupplierOrder> implements SupplierOrderDAO {

    private UUID getSupplierOrderId(SupplierOrder supplierOrder) {
        return supplierOrder.getId();
    }

    private OrderState getSupplierOrderState(SupplierOrder supplierOrder) {
        return supplierOrder.getState();
    }

    private SupplierOrder copy(SupplierOrder supplierOrder) {
        return supplierOrder.clone();
    }

    private void saveNewSupplierOrder(SupplierOrder supplierOrder) {
        this.saveNewEntity(supplierOrder, this::loadSupplierOrder, this::getSupplierOrderId, this::copy);
    }

    private void updateSupplierOrder(SupplierOrder supplierOrder) {
        this.updateEntity(supplierOrder, this::loadSupplierOrder, this::getSupplierOrderId);
    }

    private void deleteSupplierOrder(SupplierOrder supplierOrder) {
        this.deleteEntity(supplierOrder, this::loadSupplierOrder, this::getSupplierOrderId);
    }

    @Override
    public SupplierOrder loadSupplierOrder(UUID id) {
        return this.findFromCache(id, this::getSupplierOrderId);
    }

    @Override
    public List<SupplierOrder> loadByState(OrderState state) {
        return this.findMatchesFromCache(state, this::getSupplierOrderState);
    }

    @Override
    public List<SupplierOrder> loadAll() {
        return List.copyOf(this.getCache());
    }

    @Override
    public void executeTransaction(List<Statement<SupplierOrder>> statements) throws CacheIntegrityException {
        this.executeTransaction(statements, this::saveNewSupplierOrder, this::updateSupplierOrder,
                this::deleteSupplierOrder);
    }

    @Override
    public void keepIntegrity(List<Statement<SupplierOrder>> statements) throws CacheIntegrityException {
        this.keepIntegrity(statements, this::saveNewSupplierOrder, this::updateSupplierOrder,
                this::deleteSupplierOrder);
    }
}
