package com.heetch.countrypicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class CountryPickerDialog extends AppCompatDialog {

    private List<Country> countries;
    private CountryPickerCallbacks callbacks;
    private ListView listview;
    private String headingCountryCode;
    private boolean showDialingCode;

    public CountryPickerDialog(Context context, CountryPickerCallbacks callbacks) {
        this(context, callbacks, null, true);
    }

    public CountryPickerDialog(Context context, CountryPickerCallbacks callbacks, @Nullable String headingCountryCode) {
        this(context, callbacks, headingCountryCode, true);
    }

    /**
     * You can set the heading country in headingCountryCode to show
     * your favorite country as the head of the list
     * @param context
     * @param callbacks
     * @param headingCountryCode
     */
    public CountryPickerDialog(Context context, CountryPickerCallbacks callbacks,
                               @Nullable String headingCountryCode, boolean showDialingCode) {
        super(context);
        this.callbacks = callbacks;
        this.headingCountryCode = headingCountryCode;
        this.showDialingCode = showDialingCode;
        countries = Utils.parseCountries(Utils.getCountriesJSON(this.getContext()));
        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return new Locale(getContext().getResources().getConfiguration().locale.getLanguage(),
                        country1.getIsoCode()).getDisplayCountry().compareTo(
                        new Locale(getContext().getResources().getConfiguration().locale.getLanguage(),
                                country2.getIsoCode()).getDisplayCountry());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_picker);
        ViewCompat.setElevation(getWindow().getDecorView(), 3);
        listview = (ListView) findViewById(R.id.country_picker_listview);

        CountryListAdapter adapter = new CountryListAdapter(this.getContext(), countries, showDialingCode);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hide();
                Country country = countries.get(position);
                callbacks.onCountrySelected(country, Utils.getMipmapResId(getContext(),
                        country.getIsoCode().toLowerCase(Locale.ENGLISH) + "_flag"));
            }
        });

        scrollToHeadingCountry();
    }

    private void scrollToHeadingCountry() {
        if (headingCountryCode != null) {
            for (int i = 0; i < listview.getCount(); i++) {
                if (((Country) listview.getItemAtPosition(i)).getIsoCode().toLowerCase()
                        .equals(headingCountryCode.toLowerCase())) {
                    listview.setSelection(i);
                }
            }
        }
    }

    public Country getCountryFromIsoCode(String isoCode) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getIsoCode().equals(isoCode.toUpperCase())) {
                return countries.get(i);
            }
        }
        return null;
    }
}
