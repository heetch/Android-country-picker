package com.heetch.countrypicker;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Collator;
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
    private RecyclerView recyclerView;
    private String headingCountryCode;
    private boolean showDialingCode;
    private CountryListAdapter adapter;

    public CountryPickerDialog(Context context, CountryPickerCallbacks callbacks) {
        this(context, callbacks, null, true);
    }

    public CountryPickerDialog(Context context, CountryPickerCallbacks callbacks, @Nullable String headingCountryCode) {
        this(context, callbacks, headingCountryCode, true);
    }

    /**
     * You can set the heading country in headingCountryCode to show
     * your favorite country as the head of the list
     *
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
                final Locale locale = getContext().getResources().getConfiguration().locale;
                final Collator collator = Collator.getInstance(locale);
                collator.setStrength(Collator.PRIMARY);
                return collator.compare(
                        new Locale(locale.getLanguage(), country1.getIsoCode()).getDisplayCountry(),
                        new Locale(locale.getLanguage(), country2.getIsoCode()).getDisplayCountry());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_picker);
        ViewCompat.setElevation(getWindow().getDecorView(), 3);
        recyclerView = findViewById(R.id.country_picker_listview);

        adapter = new CountryListAdapter(this.getContext(), countries,
                showDialingCode, new CountryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Country country) {
                hide();
                callbacks.onCountrySelected(country, Utils.getMipmapResId(getContext(),
                        country.getIsoCode().toLowerCase(Locale.ENGLISH) + "_flag"));
            }
        });
        recyclerView.setAdapter(adapter);

        //scrollToHeadingCountry();
    }

    /*private void scrollToHeadingCountry() {
        if (headingCountryCode != null) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (((Country) recyclerView.getItemAtPosition(i)).getIsoCode().toLowerCase()
                        .equals(headingCountryCode.toLowerCase())) {
                    recyclerView.setSelection(i);
                }
            }
        }
    }*/

    public Country getCountryFromIsoCode(String isoCode) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getIsoCode().equals(isoCode.toUpperCase())) {
                return countries.get(i);
            }
        }
        return null;
    }
}
