package it.OrderFlow.Exceptions;

public class CacheIntegrityException extends Exception {

    public CacheIntegrityException() {
        super("There was a fatal error while trying to keep your data integrity. " +
                "There may be discrepancies in your cache");
    }

    public CacheIntegrityException(Throwable cause) {
        super("There was a fatal error while trying to keep your data integrity. " +
                "There may be discrepancies in your cache", cause);
    }
}
