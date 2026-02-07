package it.orderflow.dao;

import it.orderflow.control.Statement;
import it.orderflow.exceptions.PersistenceException;

import java.util.List;

public interface TransactionControl<T> {

    void executeTransaction(List<Statement<T>> statements) throws PersistenceException;

    void keepIntegrity(List<Statement<T>> statements) throws PersistenceException;
}
