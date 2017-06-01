package io.xsor.countrypicker;


public interface CountryPickerCallback {
    void onCountrySelected(Country country, int flagResId);
}
