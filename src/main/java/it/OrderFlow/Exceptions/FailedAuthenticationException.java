package it.OrderFlow.Exceptions;

public class FailedAuthenticationException extends Exception {

    public FailedAuthenticationException() {
        super("There is no employee with these credentials");
    }
}
