package it.orderflow.dao.cache;

import it.orderflow.control.Statement;
import it.orderflow.exceptions.CacheIntegrityException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class CacheGeneralDAO<T> {

    private final List<T> entities;

    protected CacheGeneralDAO() {
        this.entities = new ArrayList<>();
    }

    protected List<T> getCache() {
        return this.entities;
    }

    protected <S> T findFromCache(S key, Function<T, S> getEntityKeyMethod) {
        for (T entity : this.getCache()) {
            if (getEntityKeyMethod.apply(entity).equals(key)) return entity;
        }
        return null;
    }

    protected <S> List<T> findMatchesFromCache(S attributeToMatch, Function<T, S> getEntityAttributeToMatchMethod) {
        List<T> results = new ArrayList<>();
        for (T entity : this.getCache()) {
            if (getEntityAttributeToMatchMethod.apply(entity).equals(attributeToMatch)) results.add(entity);
        }
        return results;
    }

    protected <S, U> List<T> findMatchesFromCache(S attributeToMatch1, Function<T, S> getEntityAttributeToMatchMethod1,
                                                  U attributeToMatch2, Function<T, U> getEntityAttributeToMatchMethod2) {
        List<T> results = new ArrayList<>();
        for (T entity : this.getCache()) {
            if (getEntityAttributeToMatchMethod1.apply(entity).equals(attributeToMatch1) &&
                    getEntityAttributeToMatchMethod2.apply(entity).equals(attributeToMatch2)) results.add(entity);
        }
        return results;
    }

    protected <S> void saveNewEntity(T entity,
                                     Function<S, T> loadEntityMethod,
                                     Function<T, S> getEntityKeyMethod,
                                     UnaryOperator<T> copyEntityMethod) {
        T entityById = loadEntityMethod.apply(getEntityKeyMethod.apply(entity));

        if (entityById == null) {
            this.getCache().add(copyEntityMethod.apply(entity));
        }
    }

    protected <S> void updateEntity(T entity,
                                    Function<S, T> findEntityMethod,
                                    Function<T, S> getEntityKeyMethod) {
        T entityById = findEntityMethod.apply(getEntityKeyMethod.apply(entity));

        if (entityById != null) {
            this.getCache().set(this.getCache().indexOf(entityById), entity);
        }
    }

    protected <S> void deleteEntity(T entity,
                                    Function<S, T> findEntityMethod,
                                    Function<T, S> getEntityKeyMethod) {
        T entityById = findEntityMethod.apply(getEntityKeyMethod.apply(entity));

        if (entityById != null) this.getCache().remove(entityById);
    }

    private void rollback(List<Statement<T>> statements,
                          Consumer<T> saveEntityMethod,
                          Consumer<T> updateEntityMethod,
                          Consumer<T> deleteEntityMethod)
            throws CacheIntegrityException {
        try {
            for (Statement<T> statement : statements.reversed()) {
                if (statement.isCompleted()) {
                    switch (statement.getStatementType()) {
                        case DELETE -> saveEntityMethod.accept(statement.getNewEntity());
                        case UPDATE -> updateEntityMethod.accept(statement.getOldEntity());
                        case SAVE -> deleteEntityMethod.accept(statement.getNewEntity());
                    }

                    statement.reverted();
                }
            }
            // all completed statement reverted
        } catch (Exception e) {
            throw new CacheIntegrityException(e);
        }
    }

    protected void executeTransaction(List<Statement<T>> statements,
                                      Consumer<T> saveEntityMethod,
                                      Consumer<T> updateEntityMethod,
                                      Consumer<T> deleteEntityMethod)
            throws CacheIntegrityException {
        try {
            for (Statement<T> statement : statements) {
                switch (statement.getStatementType()) {
                    case SAVE -> saveEntityMethod.accept(statement.getNewEntity());
                    case UPDATE -> updateEntityMethod.accept(statement.getNewEntity());
                    case DELETE -> deleteEntityMethod.accept(statement.getNewEntity());
                }

                statement.completed();
            }
            // all statements successful
        } catch (Exception e) {
            // one statement failed. Rollback to previous state
            this.rollback(statements, saveEntityMethod, updateEntityMethod, deleteEntityMethod);

            throw e;
        }
    }

    protected void keepIntegrity(List<Statement<T>> statements,
                                 Consumer<T> saveEntityMethod,
                                 Consumer<T> updateEntityMethod,
                                 Consumer<T> deleteEntityMethod)
            throws CacheIntegrityException {
        // check if all the statement of this dao have already been completed
        boolean result = true;
        for (Statement<T> statement : statements) {
            if (!statement.isCompleted()) {
                result = false;
                break;
            }
        }
        // if they have all been completed we have to revert them in the reversed order
        if (result) {
            this.rollback(statements, saveEntityMethod, updateEntityMethod, deleteEntityMethod);
        }
    }
}
