package it.orderflow.beans;

import it.orderflow.model.Address;

public class AddressBean {

    private String streetAddress;
    private String cap;
    private String city;
    private String province;

    public AddressBean() {
    }

    public AddressBean(Address address) {
        this.streetAddress = address.getStreetAddress();
        this.cap = address.getCap();
        this.city = address.getCity();
        this.province = address.getProvince();
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCap() {
        return this.cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
