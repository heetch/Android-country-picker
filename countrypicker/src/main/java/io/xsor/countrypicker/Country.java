package io.xsor.countrypicker;

import java.util.Locale;

@SuppressWarnings("unused")
public class Country {
    private String isoCode;
    private String dialingCode;

    public Country() {}

    public Country(String isoCode, String dialingCode) {
        this.isoCode = isoCode.toLowerCase();
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

    public int getDialingCodeInt() {
        return Integer.valueOf(dialingCode);
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String  getCountryName() {
		return new Locale(Locale.getDefault().getLanguage(),this.getIsoCode()).getDisplayCountry();

	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return isoCode != null ? isoCode.equals(country.isoCode) : country.isoCode == null;

    }

    @Override
    public int hashCode() {
        return isoCode != null ? isoCode.hashCode() : 0;
    }
}
