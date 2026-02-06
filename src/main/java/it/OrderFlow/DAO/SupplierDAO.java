package it.OrderFlow.DAO;

import it.OrderFlow.Model.Supplier;

import java.util.List;

public interface SupplierDAO extends TransactionControl<Supplier> {

    Supplier loadSupplier(String email) throws Exception;

    List<Supplier> loadAll() throws Exception;
}
