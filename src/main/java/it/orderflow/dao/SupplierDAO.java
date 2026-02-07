package it.orderflow.dao;

import it.orderflow.exceptions.DatabaseException;
import it.orderflow.model.Supplier;

import java.util.List;

public interface SupplierDAO extends TransactionControl<Supplier> {

    Supplier loadSupplier(String email) throws DatabaseException;

    List<Supplier> loadAll() throws DatabaseException;
}
