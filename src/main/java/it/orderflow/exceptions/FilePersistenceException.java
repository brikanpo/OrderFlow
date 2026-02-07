package it.orderflow.exceptions;

public class FilePersistenceException extends PersistenceException {

    public FilePersistenceException(FilePersistenceException.ErrorType type) {
        super(type.defaultMessage);
    }

    public FilePersistenceException(FilePersistenceException.ErrorType type, EntityException.Entity entity) {
        super(type.defaultMessage + entity.getMessage());
    }

    public FilePersistenceException(FilePersistenceException.ErrorType type, EntityException.Entity entity, Throwable cause) {
        super(type.defaultMessage + entity.getMessage(), cause);
    }

    public FilePersistenceException(FilePersistenceException.ErrorType type, Throwable cause) {
        super(type.defaultMessage, cause);
    }

    public enum ErrorType {
        INTEGRITY("There was a fatal error while restoring data integrity. Check manually your file for discrepancies"),
        WRITE("There was a problem while writing to the persistence file the "),
        READ("There was a problem while reading from the persistence file the "),
        NOT_FOUND("There is no persistence file for the ");

        private final String defaultMessage;

        ErrorType(String message) {
            this.defaultMessage = message;
        }
    }
}
