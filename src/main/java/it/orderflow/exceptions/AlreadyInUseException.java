package it.orderflow.exceptions;

public class AlreadyInUseException extends EntityException {

    public AlreadyInUseException(Entity entity, Param param) {
        super(param.defaultMessage + entity.getMessage());
    }

    public enum Param {
        CODE("This combination of attributes is already associated with another "),
        EMAIL("This email is already associated with another "),
        NAME("This name is already associated with another ");

        private final String defaultMessage;

        Param(String message) {
            this.defaultMessage = message;
        }
    }
}
