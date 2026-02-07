package it.orderflow.control;

import it.orderflow.dao.TransactionControl;
import it.orderflow.exceptions.CacheIntegrityException;
import it.orderflow.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionSafeController extends RootController {

    private Map<TransactionControl<?>, List<Statement<?>>> transaction;

    public void startOperation() {
        this.transaction = new HashMap<>();
    }

    public <T> void addStatement(TransactionControl<T> dao, Statement<T> statement) {
        if (!this.transaction.containsKey(dao)) {
            this.transaction.put(dao, new ArrayList<>());
        }
        this.transaction.get(dao).add(statement);
    }

    public void endOperation() throws CacheIntegrityException, DatabaseException {
        try {
            for (Map.Entry<TransactionControl<?>, List<Statement<?>>> entry : this.transaction.entrySet()) {
                executeHelper(entry.getKey(), entry.getValue());
            }
        } catch (DatabaseException e) {
            for (Map.Entry<TransactionControl<?>, List<Statement<?>>> entry : this.transaction.entrySet()) {
                rollbackHelper(entry.getKey(), entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void executeHelper(TransactionControl<T> dao, List<Statement<?>> statements) throws DatabaseException {
        dao.executeTransaction((List<Statement<T>>) (List<?>) statements);
    }

    @SuppressWarnings("unchecked")
    private <T> void rollbackHelper(TransactionControl<T> dao, List<Statement<?>> statements) throws CacheIntegrityException, DatabaseException {
        dao.keepIntegrity((List<Statement<T>>) (List<?>) statements);
    }
}
