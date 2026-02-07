package it.orderflow.dao;

import it.orderflow.model.OrderState;
import it.orderflow.model.SupplierOrder;

import java.util.List;
import java.util.UUID;

public interface SupplierOrderDAO extends TransactionControl<SupplierOrder> {

    SupplierOrder loadSupplierOrder(UUID id) throws Exception;

    List<SupplierOrder> loadByState(OrderState state) throws Exception;

    List<SupplierOrder> loadAll() throws Exception;
}
