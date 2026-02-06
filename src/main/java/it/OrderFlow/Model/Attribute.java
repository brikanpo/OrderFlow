package it.OrderFlow.Model;

public class Attribute implements Cloneable {

    private String id;
    private String value;

    public Attribute(String id) {
        this(id, null);
    }

    public Attribute(String id, String value) {
        this.setId(id);
        this.setValue(value);
    }

    public String getId() {
        return this.id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    @Override
    public Attribute clone() {
        try {
            return (Attribute) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
