package it.OrderFlow.DAO.DBMS;

import it.OrderFlow.Control.Statement;
import it.OrderFlow.Exceptions.DatabaseException;
import it.OrderFlow.Exceptions.EntityException;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DBMSGeneralDAO<T> {

    private final List<T> entities;
    private final DBMSConnectionPool connectionPool;

    protected DBMSGeneralDAO() {
        this.entities = new ArrayList<>();
        this.connectionPool = DBMSConnectionPool.getInstance();
    }

    protected byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    protected UUID bytesToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return new UUID(bb.getLong(), bb.getLong());
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

    private <S> void setPreparedStatement(PreparedStatement pstmt, int index, S attribute) throws SQLException {
        if (attribute instanceof UUID) {
            pstmt.setBytes(index, this.uuidToBytes((UUID) attribute));
        } else if (attribute instanceof String) {
            pstmt.setString(index, (String) attribute);
        }
    }

    protected <S> T findFromPersistence(String tableName,
                                        String keyName, S key,
                                        ThrowingFunction<ResultSet, T, DatabaseException> getEntityMethod,
                                        EntityException.Entity entityType)
            throws DatabaseException {
        PreparedStatement pstmt = null;

        Connection conn = this.connectionPool.getConnection();

        T result;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + keyName + " = ?;");

            this.setPreparedStatement(pstmt, 1, key);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                result = null;
            } else {
                result = getEntityMethod.apply(rs);
            }
            rs.close();

            return result;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.SELECT, entityType, e);
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    this.connectionPool.releaseConnection(conn);
            } catch (SQLException ignore) {
            }
        }
    }

    protected <S> List<T> findMatchesFromPersistence(String tableName,
                                                     String attributeName, S attributeToMatch,
                                                     ThrowingFunction<ResultSet, List<T>, DatabaseException> getMultipleEntityMethod,
                                                     EntityException.Entity entityType)
            throws DatabaseException {
        PreparedStatement pstmt = null;

        Connection conn = this.connectionPool.getConnection();

        List<T> result;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + attributeName + " = ?;");

            this.setPreparedStatement(pstmt, 1, attributeToMatch);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                result = new ArrayList<>();
            } else {
                result = getMultipleEntityMethod.apply(rs);
            }
            rs.close();

            return result;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.SELECT, entityType, e);
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    this.connectionPool.releaseConnection(conn);
            } catch (SQLException ignore) {
            }
        }
    }

    protected <S, U> List<T> findMatchesFromPersistence(String tableName,
                                                        String attributeName1, S attributeToMatch1,
                                                        String attributeName2, U attributeToMatch2,
                                                        ThrowingFunction<ResultSet, List<T>, DatabaseException> getMultipleEntityMethod,
                                                        EntityException.Entity entityType)
            throws DatabaseException {
        PreparedStatement pstmt = null;

        Connection conn = this.connectionPool.getConnection();

        List<T> result;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + attributeName1 + " = ? AND " + attributeName2 + " = ?;");

            this.setPreparedStatement(pstmt, 1, attributeToMatch1);
            this.setPreparedStatement(pstmt, 2, attributeToMatch2);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                result = new ArrayList<>();
            } else {
                result = getMultipleEntityMethod.apply(rs);
            }
            rs.close();

            return result;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.SELECT, entityType, e);
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    this.connectionPool.releaseConnection(conn);
            } catch (SQLException ignore) {
            }
        }
    }

    protected List<T> getEntityList(ResultSet rs,
                                    ThrowingFunction<ResultSet, T, DatabaseException> getEntityMethod,
                                    EntityException.Entity entityType)
            throws DatabaseException {
        try {

            List<T> results = new ArrayList<>();


            do {
                T entity = getEntityMethod.apply(rs);

                results.add(entity);

            } while (rs.next());

            return results;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.TRANSLATE_FROM, entityType, e);
        }
    }

    protected <S> T findSingleResult(S key,
                                     Function<S, T> findEntityFromCacheMethod,
                                     ThrowingFunction<S, T, DatabaseException> findEntityFromPersistenceMethod)
            throws DatabaseException {
        T entity = findEntityFromCacheMethod.apply(key);
        if (entity == null) {
            entity = findEntityFromPersistenceMethod.apply(key);
            if (entity != null) {
                this.getCache().add(entity);
            }
        }
        return entity;
    }

    protected <S, U> List<T> findMultipleResults(S attributeToMatch,
                                                 Function<T, U> getEntityKeyMethod,
                                                 Function<S, List<T>> findMatchesFromCacheMethod,
                                                 ThrowingFunction<S, List<T>, DatabaseException> findMatchesFromPersistenceMethod)
            throws DatabaseException {
        List<T> resultsCache = findMatchesFromCacheMethod.apply(attributeToMatch);
        List<T> resultsPersistence = findMatchesFromPersistenceMethod.apply(attributeToMatch);
        for (T entityPersistence : resultsPersistence) {
            boolean found = false;
            for (T entityCache : resultsCache) {
                if (getEntityKeyMethod.apply(entityPersistence).equals(getEntityKeyMethod.apply(entityCache))) {
                    found = true;
                }
            }
            if (!found) {
                this.getCache().add(entityPersistence);
                resultsCache.add(entityPersistence);
            }
        }
        return resultsCache;
    }

    protected <S, U, V> List<T> findMultipleResults(S attributeToMatch1, U attributeToMatch2,
                                                    Function<T, V> getEntityKeyMethod,
                                                    BiFunction<S, U, List<T>> findMatchesFromCacheMethod,
                                                    ThrowingBiFunction<S, U, List<T>, DatabaseException> findMatchesFromPersistenceMethod)
            throws DatabaseException {
        List<T> resultsCache = findMatchesFromCacheMethod.apply(attributeToMatch1, attributeToMatch2);
        List<T> resultsPersistence = findMatchesFromPersistenceMethod.apply(attributeToMatch1, attributeToMatch2);
        for (T entityPersistence : resultsPersistence) {
            boolean found = false;
            for (T entityCache : resultsCache) {
                if (getEntityKeyMethod.apply(entityPersistence).equals(getEntityKeyMethod.apply(entityCache))) {
                    found = true;
                }
            }
            if (!found) {
                this.getCache().add(entityPersistence);
                resultsCache.add(entityPersistence);
            }
        }
        return resultsCache;
    }

    protected <S> void saveNewEntity(T entity,
                                     ThrowingFunction<S, T, DatabaseException> loadEntityMethod,
                                     Function<T, S> getEntityKeyMethod,
                                     Function<T, T> copyEntityMethod,
                                     String statement,
                                     ThrowingTriConsumer<PreparedStatement, Statement.Type, T, DatabaseException> loadPreparedStatementMethod,
                                     EntityException.Entity entityType)
            throws DatabaseException {
        T entityById = loadEntityMethod.apply(getEntityKeyMethod.apply(entity));

        if (entityById == null) {
            this.getCache().add(copyEntityMethod.apply(entity));

            PreparedStatement pstmt = null;

            Connection conn = this.connectionPool.getConnection();

            try {
                pstmt = conn.prepareStatement(statement);

                Statement.Type statementType = switch (statement.charAt(0)) {
                    case 'I' -> Statement.Type.SAVE;
                    case 'U' -> Statement.Type.UPDATE;
                    case 'D' -> Statement.Type.DELETE;
                    default -> null;
                };

                loadPreparedStatementMethod.accept(pstmt, statementType, copyEntityMethod.apply(entity));

                pstmt.executeUpdate();

            } catch (SQLException e) {
                throw new DatabaseException(DatabaseException.ErrorType.SAVE, entityType, e);
            } finally {
                try {
                    if (pstmt != null)
                        pstmt.close();
                    if (conn != null)
                        this.connectionPool.releaseConnection(conn);
                } catch (SQLException ignore) {
                }
            }
        }
    }

    protected <S> void updateEntity(T entity,
                                    ThrowingFunction<S, T, DatabaseException> findEntityMethod,
                                    Function<T, S> getEntityKeyMethod, String statement,
                                    ThrowingTriConsumer<PreparedStatement, Statement.Type, T, DatabaseException> loadPreparedStatementMethod,
                                    EntityException.Entity entityType)
            throws DatabaseException {
        T entityById = findEntityMethod.apply(getEntityKeyMethod.apply(entity));

        if (entityById != null) {
            this.getCache().set(this.getCache().indexOf(entityById), entity);

            PreparedStatement pstmt = null;

            Connection conn = this.connectionPool.getConnection();

            try {
                pstmt = conn.prepareStatement(statement);

                Statement.Type statementType = switch (statement.charAt(0)) {
                    case 'I' -> Statement.Type.SAVE;
                    case 'U' -> Statement.Type.UPDATE;
                    case 'D' -> Statement.Type.DELETE;
                    default -> null;
                };

                loadPreparedStatementMethod.accept(pstmt, statementType, entity);

                pstmt.executeUpdate();

            } catch (SQLException e) {
                throw new DatabaseException(DatabaseException.ErrorType.UPDATE, entityType, e);
            } finally {
                try {
                    if (pstmt != null)
                        pstmt.close();
                    if (conn != null)
                        this.connectionPool.releaseConnection(conn);
                } catch (SQLException ignore) {
                }
            }
        }
    }

    protected <S> void deleteEntity(T entity,
                                    ThrowingFunction<S, T, DatabaseException> findEntityMethod,
                                    Function<T, S> getEntityKeyMethod, String statement,
                                    ThrowingTriConsumer<PreparedStatement, Statement.Type, T, DatabaseException> loadPreparedStatementMethod,
                                    EntityException.Entity entityType)
            throws DatabaseException {
        T entityById = findEntityMethod.apply(getEntityKeyMethod.apply(entity));

        if (entityById != null) {
            this.getCache().remove(entityById);

            PreparedStatement pstmt = null;

            Connection conn = this.connectionPool.getConnection();

            try {
                pstmt = conn.prepareStatement(statement);

                Statement.Type statementType = switch (statement.charAt(0)) {
                    case 'I' -> Statement.Type.SAVE;
                    case 'U' -> Statement.Type.UPDATE;
                    case 'D' -> Statement.Type.DELETE;
                    default -> null;
                };

                loadPreparedStatementMethod.accept(pstmt, statementType, entityById);

                pstmt.executeUpdate();

            } catch (SQLException e) {
                throw new DatabaseException(DatabaseException.ErrorType.DELETE, entityType, e);
            } finally {
                try {
                    if (pstmt != null)
                        pstmt.close();
                    if (conn != null)
                        this.connectionPool.releaseConnection(conn);
                } catch (SQLException ignore) {
                }
            }
        }
    }

    protected List<T> loadAll(String tableName,
                              ThrowingFunction<ResultSet, T, DatabaseException> getEntityFunction,
                              EntityException.Entity entityType)
            throws DatabaseException {
        java.sql.Statement stmt = null;

        Connection conn = this.connectionPool.getConnection();

        List<T> list = new ArrayList<>();

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");

            if (rs.first()) {
                do {
                    T entity = getEntityFunction.apply(rs);

                    list.add(entity);

                } while (rs.next());
            }
            rs.close();

            return list;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.SELECT, entityType, e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    this.connectionPool.releaseConnection(conn);
            } catch (SQLException ignore) {
            }
        }
    }

    protected boolean isEmpty(String tableName, EntityException.Entity entityType) throws DatabaseException {
        java.sql.Statement stmt = null;

        Connection conn = this.connectionPool.getConnection();

        boolean result;

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM " + tableName + " LIMIT 1;");

            result = !rs.next();

            rs.close();

            return result;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.SELECT, entityType, e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    this.connectionPool.releaseConnection(conn);
            } catch (SQLException ignore) {
            }
        }
    }

    private void rollback(List<Statement<T>> statements,
                          ThrowingConsumer<T, DatabaseException> saveEntityMethod,
                          ThrowingConsumer<T, DatabaseException> updateEntityMethod,
                          ThrowingConsumer<T, DatabaseException> deleteEntityMethod)
            throws DatabaseException {
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
            throw new DatabaseException(DatabaseException.ErrorType.INTEGRITY, e);
        }
    }

    protected void executeTransaction(List<Statement<T>> statements,
                                      ThrowingConsumer<T, DatabaseException> saveEntityMethod,
                                      ThrowingConsumer<T, DatabaseException> updateEntityMethod,
                                      ThrowingConsumer<T, DatabaseException> deleteEntityMethod)
            throws DatabaseException {
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
        } catch (DatabaseException e) {
            // one statement failed. Rollback to previous state
            this.rollback(statements, saveEntityMethod, updateEntityMethod, deleteEntityMethod);

            throw e;
        }
    }

    protected void keepIntegrity(List<Statement<T>> statements,
                                 ThrowingConsumer<T, DatabaseException> saveEntityMethod,
                                 ThrowingConsumer<T, DatabaseException> updateEntityMethod,
                                 ThrowingConsumer<T, DatabaseException> deleteEntityMethod)
            throws DatabaseException {
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

    @FunctionalInterface
    public interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingBiFunction<T, U, R, E extends Exception> {
        R apply(T t, U u) throws E;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingTriConsumer<T, U, V, E extends Exception> {
        void accept(T t, U u, V v) throws E;
    }
}
