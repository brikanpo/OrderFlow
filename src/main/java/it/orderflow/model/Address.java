package it.orderflow.model;

public class Address {

    private final String streetAddress;
    private final String cap;
    private final String city;
    private final String province;

    public Address(String streetAddress, String cap, String city, String province) {
        this.streetAddress = streetAddress;
        this.cap = cap;
        this.city = city;
        this.province = province;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public String getCap() {
        return this.cap;
    }

    public String getCity() {
        return this.city;
    }

    public String getProvince() {
        return this.province;
    }

    public Address copy() {
        return new Address(this.getStreetAddress(), this.getCap(), this.getCity(), this.getProvince());
    }
}
