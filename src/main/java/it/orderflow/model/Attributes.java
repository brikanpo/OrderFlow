package it.orderflow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Attributes {

    private List<Attribute> attributeList;

    public Attributes() {
        this.setAttributeList(new ArrayList<>());
    }

    public Attributes(List<String> attrIds, List<String> attrVal) {
        this();
        for (int i = 0; i < attrIds.size(); i++) {
            this.addAttributeIdAndValue(attrIds.get(i), attrVal.get(i));
        }
    }

    public List<Attribute> getAttributeList() {
        return this.attributeList;
    }

    private void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public void addAttributeId(String attrId) {
        if (this.getAttribute(attrId) == null) {
            this.getAttributeList().add(new Attribute(attrId));
        }
    }

    public void addAttributeIdAndValue(String attrId, String attrVal) {
        if (this.getAttribute(attrId) == null) {
            this.getAttributeList().add(new Attribute(attrId, attrVal));
        }
    }

    public Attribute getAttribute(String attrId) {
        for (int i = 0; i < this.getAttributeList().size(); i++) {
            Attribute attribute = this.getAttributeList().get(i);
            if (attribute.getId().equals(attrId)) {
                return attribute;
            }
        }
        return null;
    }

    public List<String> getAttributesId() {
        List<String> result = new ArrayList<>();
        for (Attribute attr : this.getAttributeList()) {
            result.add(attr.getId());
        }
        return result;
    }

    public List<String> getAttributesValues() {
        List<String> result = new ArrayList<>();
        for (Attribute attr : this.getAttributeList()) {
            result.add(attr.getValue());
        }
        return result;
    }

    public void removeAttribute(String attrId) {
        Attribute attribute = this.getAttribute(attrId);
        if (attribute != null) {
            this.getAttributeList().remove(attribute);
        }
    }

    public Attributes copy() {
        Attributes attributes = new Attributes();
        attributes.setAttributeList(this.getAttributeList().stream()
                .map(Attribute::copy)
                .collect(Collectors.toCollection(ArrayList::new)));
        return attributes;
    }
}
