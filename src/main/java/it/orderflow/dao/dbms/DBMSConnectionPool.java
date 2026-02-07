package it.orderflow.dao.dbms;

import it.orderflow.ConfigManager;
import it.orderflow.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DBMSConnectionPool {

    private static boolean isInitialized = false;
    private final String user;
    private final String password;
    private final String dbUrl;
    private final BlockingQueue<Connection> pool;

    private DBMSConnectionPool() {
        ConfigManager configManager = ConfigManager.getInstance();

        user = configManager.getProperty("db.user");
        password = configManager.getProperty("db.password");
        dbUrl = configManager.getProperty("db.url");
        int maxConnections = Integer.parseUnsignedInt(configManager.getProperty("db.maxConnections"));

        this.pool = new LinkedBlockingQueue<>(maxConnections);

        try {
            Class.forName(configManager.getProperty("db.driver"));

            for (int i = 0; i < maxConnections; i++) {
                this.pool.add(this.createNewConnection());
            }

        } catch (Exception e) {
            throw new IllegalStateException("Connection pool not initialized", e);
        }
    }

    public static DBMSConnectionPool getInstance() {
        isInitialized = true;
        return Holder.INSTANCE;
    }

    public static boolean isOpen() {
        return isInitialized;
    }

    public Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }

    public Connection getConnection() throws DatabaseException {
        try {
            Connection conn = pool.poll(2, TimeUnit.SECONDS);
            if (conn == null) throw new DatabaseException(DatabaseException.ErrorType.NO_CONNECTION_AVAILABLE);

            if (!conn.isValid(2)) {
                this.close(conn);
                conn = this.createNewConnection();
            }

            return conn;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException(DatabaseException.ErrorType.START_CONNECTION, e);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.CONFIG_DB);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null && !pool.offer(connection)) {
            close(connection);
        }
    }

    private void close(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException ignored) {
            //Empty because if close() fails there is nothing to be done about it
        }
    }

    private static class Holder {
        private static final DBMSConnectionPool INSTANCE = new DBMSConnectionPool();
    }

    public synchronized void closeAll() {
        Connection conn;
        while ((conn = pool.poll()) != null) {
            close(conn);
        }
    }
}
