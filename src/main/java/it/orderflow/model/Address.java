package it.orderflow.model;

public class Address implements Cloneable {

    private String address;
    private String cap;
    private String city;
    private String province;

    public Address(String address, String cap, String city, String province) {
        this.address = address;
        this.cap = cap;
        this.city = city;
        this.province = province;
    }

    public String getAddress() {
        return this.address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return this.cap;
    }

    private void setCap(String cap) {
        this.cap = cap;
    }

    public String getCity() {
        return this.city;
    }

    private void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    private void setProvince(String province) {
        this.province = province;
    }

    @Override
    public Address clone() {
        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
