package it.OrderFlow.Exceptions;

public class InvalidStateException extends IllegalStateException {

    public InvalidStateException(StateType type) {
        super(type.defaultMessage);
    }

    public enum StateType {
        TO_WAITING("There was a problem modifying the order"),
        TO_READY("There was a problem in marking the order as ready"),
        TO_CLOSED("There was a problem in marking the order as closed");

        private final String defaultMessage;

        StateType(String message) {
            this.defaultMessage = message;
        }
    }
}
