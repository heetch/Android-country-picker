package io.xsor.countrypicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CountryPickerDialog extends AppCompatDialog {

    private List<Country> countries;
    CountryListAdapter adapter;
    private CountryPickerCallback callback;
    private RecyclerView recyclerView;

    private String scrollToCountryCode;
    private boolean showDialingCode = false;
    private boolean showRoundFlags = false;

    /**
     *
     * @param context The calling activity or context
     * @param callback The callback to be fired when a country from the list is chosen
     */
    public CountryPickerDialog(Context context, CountryPickerCallback callback) {
        super(context);
        this.callback = callback;
        countries = CountryPickerUtils.parseCountries(CountryPickerUtils.getCountriesJSON(this.getContext()));
        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                final Locale locale = Locale.getDefault();
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

        recyclerView = (RecyclerView) findViewById(R.id.rvCountriesList);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        adapter = new CountryListAdapter(countries);

        adapter.withRoundFlags(showRoundFlags);
        adapter.withDialingCode(showDialingCode);

        adapter.setCallback(callback);

        recyclerView.setAdapter(adapter);

        if(scrollToCountryCode != null) {
            scrollToHeadingCountry();
        }
    }

    public CountryPickerDialog withDialingCode(boolean showDialingCode) {
        this.showDialingCode = showDialingCode;
        if(adapter != null) {
            adapter.withDialingCode(showDialingCode);
            adapter.notifyDataSetChanged();

        }
        return this;
    }

    public CountryPickerDialog withRoundFlags(boolean showRoundFlags) {
        this.showRoundFlags = showRoundFlags;
        if(adapter != null) {
            adapter.withRoundFlags(showRoundFlags);
            adapter.notifyDataSetChanged();
        }
        return this;
    }

    public CountryPickerDialog withScrollToCountry(String countryIsoCode) {
        this.scrollToCountryCode = countryIsoCode;
        return this;
    }

    private void scrollToHeadingCountry() {
        int idx = countries.indexOf(new Country(scrollToCountryCode,null));
        if(idx > -1) {
            recyclerView.scrollToPosition(idx);
        }
    }
}
