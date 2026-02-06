package it.OrderFlow.DAO.DBMS;

import it.OrderFlow.ConfigManager;
import it.OrderFlow.Exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DBMSConnectionPool {

    private static DBMSConnectionPool instance;
    private final String USER;
    private final String PASS;
    private final String DB_URL;
    private final BlockingQueue<Connection> pool;

    private DBMSConnectionPool() {
        ConfigManager configManager = ConfigManager.getInstance();

        USER = configManager.getProperty("db.user");
        PASS = configManager.getProperty("db.password");
        DB_URL = configManager.getProperty("db.url");
        int maxConnections = Integer.parseUnsignedInt(configManager.getProperty("db.maxConnections"));

        this.pool = new LinkedBlockingQueue<>(maxConnections);

        try {
            Class.forName(configManager.getProperty("db.driver"));

            for (int i = 0; i < maxConnections; i++) {
                this.pool.add(this.createNewConnection());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DBMSConnectionPool getInstance() {
        if (instance == null) instance = new DBMSConnectionPool();
        return instance;
    }

    public static boolean isOpen() {
        return instance != null;
    }

    public Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
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
            throw new DatabaseException(DatabaseException.ErrorType.START_CONNECTION, e);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseException.ErrorType.CONFIG_DB);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            if (!pool.offer(connection)) {
                close(connection);
            }
        }
    }

    private void close(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException ignored) {
        }
    }

    public synchronized void closeAll() {
        Connection conn;
        while ((conn = pool.poll()) != null) {
            close(conn);
        }
    }
}
