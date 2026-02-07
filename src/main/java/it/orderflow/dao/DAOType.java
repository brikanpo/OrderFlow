package it.orderflow.dao;

public enum DAOType {
    CACHE,
    DBMS,
    FILE;

    public static DAOType getDAOType(String str) {
        return switch (str) {
            case "Cache" -> CACHE;
            case "DBMS" -> DBMS;
            case "File" -> FILE;
            default -> null;
        };
    }
}
