package it.OrderFlow.Beans;

import it.OrderFlow.Model.Address;

public class AddressBean {

    private String address;
    private String cap;
    private String city;
    private String province;

    public AddressBean() {
    }

    public AddressBean(Address address) {
        this.address = address.getAddress();
        this.cap = address.getCap();
        this.city = address.getCity();
        this.province = address.getProvince();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
