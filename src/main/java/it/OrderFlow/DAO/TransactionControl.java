package it.OrderFlow.DAO;

import it.OrderFlow.Control.Statement;

import java.util.List;

public interface TransactionControl<T> {

    void executeTransaction(List<Statement<T>> statements) throws Exception;

    void keepIntegrity(List<Statement<T>> statements) throws Exception;
}
