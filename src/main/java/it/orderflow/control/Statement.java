package it.orderflow.control;

import java.util.List;

public class Statement<T> {

    private final List<T> entities;
    private final Type statementType;
    private boolean isCompleted = false;
    private boolean isReverted = false;
    public Statement(List<T> entities, Type statementType) {
        this.entities = entities;
        this.statementType = statementType;
    }

    public T getNewEntity() {
        return this.entities.getFirst();
    }

    public T getOldEntity() {
        return this.entities.getLast();
    }

    public Type getStatementType() {
        return this.statementType;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void completed() {
        this.isCompleted = true;
    }

    public void reverted() {
        this.isReverted = true;
    }

    public enum Type {
        SAVE, DELETE, UPDATE
    }
}
