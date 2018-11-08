package ch.mse.dea.donteatalone.objects;

import java.util.Objects;

public class Address {

    private String address;
    private String postcode;
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public Address() {
    }

    public Address(String address, String postcode, String city, String country, double latitude, double longitude) {
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Double.compare(address1.getLatitude(), getLatitude()) == 0 &&
                Double.compare(address1.getLongitude(), getLongitude()) == 0 &&
                Objects.equals(getAddress(), address1.getAddress()) &&
                Objects.equals(getPostcode(), address1.getPostcode()) &&
                Objects.equals(getCity(), address1.getCity()) &&
                Objects.equals(getCountry(), address1.getCountry());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getAddress(), getPostcode(), getCity(), getCountry(), getLatitude(), getLongitude());
    }
}
