package it.orderflow.exceptions;

public class EmailNotSentException extends Exception {

    public EmailNotSentException(String emailAddress, Throwable cause) {
        super("There was a problem sending an email to :" + emailAddress, cause);
    }
}
