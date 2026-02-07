package it.orderflow.dao;

import it.orderflow.exceptions.PersistenceException;
import it.orderflow.model.Supplier;

import java.util.List;

public interface SupplierDAO extends TransactionControl<Supplier> {

    Supplier loadSupplier(String email) throws PersistenceException;

    List<Supplier> loadAll() throws PersistenceException;
}
