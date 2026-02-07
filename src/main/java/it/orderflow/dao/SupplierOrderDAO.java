package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.OrderState;
import it.orderflow.model.SupplierOrder;

import java.util.List;
import java.util.UUID;

public interface SupplierOrderDAO extends TransactionControl<SupplierOrder> {

    SupplierOrder loadSupplierOrder(UUID id) throws PersistenceException;

    List<SupplierOrder> loadByState(OrderState state) throws PersistenceException;

    List<SupplierOrder> loadAll() throws PersistenceException;
}
