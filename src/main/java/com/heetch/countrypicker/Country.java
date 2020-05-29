package com.heetch.countrypicker;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class Country {
    private String isoCode;
    private String dialingCode;
    private String countryName;

    public Country() {

    }

    public Country(String name, String isoCode, String dialingCode) {
        this.isoCode = isoCode;
        this.dialingCode = dialingCode;
        this.countryName = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }
}
