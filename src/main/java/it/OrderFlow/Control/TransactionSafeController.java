package it.OrderFlow.Control;

import it.OrderFlow.DAO.TransactionControl;

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

    public void endOperation() throws Exception {
        try {
            for (Map.Entry<TransactionControl<?>, List<Statement<?>>> entry : this.transaction.entrySet()) {
                executeHelper(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            for (Map.Entry<TransactionControl<?>, List<Statement<?>>> entry : this.transaction.entrySet()) {
                rollbackHelper(entry.getKey(), entry.getValue());
            }
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void executeHelper(TransactionControl<T> dao, List<Statement<?>> statements) throws Exception {
        dao.executeTransaction((List<Statement<T>>) (List<?>) statements);
    }

    @SuppressWarnings("unchecked")
    private <T> void rollbackHelper(TransactionControl<T> dao, List<Statement<?>> statements) throws Exception {
        dao.keepIntegrity((List<Statement<T>>) (List<?>) statements);
    }
}
