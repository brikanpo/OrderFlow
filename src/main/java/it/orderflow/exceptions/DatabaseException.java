package it.orderflow.exceptions;

public class DatabaseException extends Exception {

    public DatabaseException(DatabaseException.ErrorType type) {
        super(type.defaultMessage);
    }

    public DatabaseException(DatabaseException.ErrorType type, EntityException.Entity entity) {
        super(type.defaultMessage + entity.getMessage());
    }

    public DatabaseException(DatabaseException.ErrorType type, EntityException.Entity entity, Throwable cause) {
        super(type.defaultMessage + entity.getMessage(), cause);
    }

    public DatabaseException(DatabaseException.ErrorType type, Throwable cause) {
        super(type.defaultMessage, cause);
    }

    public enum ErrorType {
        CONFIG_DB("The info for the database connection are invalid"),
        INTEGRITY("There was a fatal error while restoring data integrity. Check manually your database for discrepancies"),
        NO_CONNECTION_AVAILABLE("There was no connection available to connect to the database"),
        START_CONNECTION("There was a problem initiating a connection to the database"),
        DELETE("There was a problem while deleting from the database the "),
        SAVE("There was a problem while saving to the database the "),
        SELECT("There was a problem while loading from the database the "),
        TRANSLATE_FROM("There was a problem while converting from the database format the "),
        TRANSLATE_TO("There was a problem while converting to the database format the "),
        UPDATE("There was a problem while updating to the database the ");

        private final String defaultMessage;

        ErrorType(String message) {
            this.defaultMessage = message;
        }
    }
}
