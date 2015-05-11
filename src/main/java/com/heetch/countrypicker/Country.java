package com.heetch.countrypicker;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class Country {
    private String isoCode;
    private String name;
    private String countyCode;

    public Country() {

    }

    public Country(String isoCode, String name, String countyCode) {
        this.isoCode = isoCode;
        this.name = name;
        this.countyCode = countyCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }
}
