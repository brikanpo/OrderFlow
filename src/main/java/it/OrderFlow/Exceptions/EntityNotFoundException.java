package it.OrderFlow.Exceptions;

public class EntityNotFoundException extends EntityException {

    public EntityNotFoundException(Entity entity) {
        super("This " + entity.getMessage() + " is not registered in the system");
    }
}
