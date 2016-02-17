package com.heetch.countrypicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class CountryPickerDialog extends AppCompatDialog {

    private List<Country> countries;
    private CountryPickerCallbacks callbacks;
    private ListView listview;

    public CountryPickerDialog(Context context, CountryPickerCallbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
        countries = Utils.parseCountries(Utils.getCountriesJSON(this.getContext()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_picker);
        ViewCompat.setElevation(getWindow().getDecorView(), 3);
        listview = (ListView) findViewById(R.id.country_picker_listview);

        CountryListAdapter adapter = new CountryListAdapter(this.getContext(), countries);
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
