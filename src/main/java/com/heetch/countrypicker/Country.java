package com.heetch.countrypicker;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class Country {
    private String countryCode;
    private String name;
    private String callingCode;

    public Country() {

    }

    public Country(String countryCode, String name, String callingCode) {
        this.countryCode = countryCode;
        this.name = name;
        this.callingCode = callingCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }
}
