package com.heetch.countrypicker;

import java.util.Locale;

@SuppressWarnings("unused")
public class Country {
    private String isoCode;
    private String dialingCode;

    public Country() {}

    public Country(String isoCode, String dialingCode) {
        this.isoCode = isoCode;
        this.dialingCode = dialingCode;
    }

    public String getIsoCode() {
        return isoCode;
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

    public String  getCountryName() {
		return new Locale(Locale.getDefault().getLanguage(),this.getIsoCode()).getDisplayCountry();

	}
}
