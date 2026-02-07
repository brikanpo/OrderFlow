package it.orderflow.exceptions;

public class EntityException extends Exception {

    public EntityException(String message) {
        super(message);
    }

    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public enum Entity {
        CLIENT("client"),
        CLIENT_ARTICLE("client article"),
        EMPLOYEE("employee"),
        CLIENT_ORDER("client order"),
        PRODUCT("supplier product"),
        PRODUCT_IN_STOCK("product in inventory"),
        SUPPLIER("supplier"),
        SUPPLIER_ARTICLE("supplier article"),
        SUPPLIER_ORDER("supplier order");

        private final String defaultMessage;

        Entity(String message) {
            this.defaultMessage = message;
        }

        String getMessage() {
            return this.defaultMessage;
        }
    }
}
