package it.orderflow.dao;

import it.orderflow.control.Statement;
import it.orderflow.exceptions.CacheIntegrityException;
import it.orderflow.exceptions.DatabaseException;

import java.util.List;

public interface TransactionControl<T> {

    void executeTransaction(List<Statement<T>> statements) throws DatabaseException;

    void keepIntegrity(List<Statement<T>> statements) throws CacheIntegrityException, DatabaseException;
}
