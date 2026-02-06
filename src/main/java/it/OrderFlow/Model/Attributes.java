package it.OrderFlow.Model;

import java.util.ArrayList;
import java.util.List;

public class Attributes implements Cloneable {

    private List<Attribute> attributes;

    public Attributes() {
        this.setAttributes(new ArrayList<>());
    }

    public Attributes(List<String> attrIds, List<String> attrVal) {
        this();
        for (int i = 0; i < attrIds.size(); i++) {
            this.addAttributeIdAndValue(attrIds.get(i), attrVal.get(i));
        }
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    private void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttributeId(String attrId) {
        if (this.getAttribute(attrId) == null) {
            this.getAttributes().add(new Attribute(attrId));
        }
    }

    public void addAttributeIdAndValue(String attrId, String attrVal) {
        if (this.getAttribute(attrId) == null) {
            this.getAttributes().add(new Attribute(attrId, attrVal));
        }
    }

    public Attribute getAttribute(String attrId) {
        for (int i = 0; i < this.getAttributes().size(); i++) {
            Attribute attribute = this.getAttributes().get(i);
            if (attribute.getId().equals(attrId)) {
                return attribute;
            }
        }
        return null;
    }

    public List<String> getAttributesId() {
        List<String> result = new ArrayList<>();
        for (Attribute attr : this.getAttributes()) {
            result.add(attr.getId());
        }
        return result;
    }

    public List<String> getAttributesValues() {
        List<String> result = new ArrayList<>();
        for (Attribute attr : this.getAttributes()) {
            result.add(attr.getValue());
        }
        return result;
    }

    public void removeAttribute(String attrId) {
        Attribute attribute = this.getAttribute(attrId);
        if (attribute != null) {
            this.getAttributes().remove(attribute);
        }
    }

    @Override
    public Attributes clone() {
        try {
            Attributes clone = (Attributes) super.clone();
            clone.attributes = this.attributes.stream().map(Attribute::clone).toList();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
