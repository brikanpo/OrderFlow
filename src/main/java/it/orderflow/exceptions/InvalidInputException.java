package it.orderflow.exceptions;

public class InvalidInputException extends Exception {

    public InvalidInputException(InputType type) {
        super(type.defaultMessage);
    }

    public InvalidInputException(InputType type, Throwable cause) {
        super(type.defaultMessage, cause);
    }

    public enum InputType {
        BLANK("Fill all the non optional fields"),
        CONFIG_DEFAULT("The default options for graphics or persistence are invalid"),
        DEFAULT_PASSWORD("Enter a new password different from the default password"),
        EMAIL("Enter a valid email address (e.g., something@something.something)"),
        PERCENTAGE("Enter a number between 0.00 and 1.00"),
        PRICE("Enter a valid positive price (e.g., 1.23)"),
        QUANTITY_0_OR_HIGHER("Enter a valid quantity (e.g., 0 or higher)"),
        QUANTITY_1_OR_HIGHER("Enter a valid quantity (e.g., 1 or higher)"),
        QUANTITY_SAME_OR_LOWER("Enter a valid quantity (e.g., the starting one or lower)"),
        ROLE("Enter a valid role for the user"),
        SELECTION("Enter a valid option in the selection");

        private final String defaultMessage;

        InputType(String message) {
            this.defaultMessage = message;
        }
    }
}
