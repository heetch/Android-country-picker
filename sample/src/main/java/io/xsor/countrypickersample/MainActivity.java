package io.xsor.countrypickersample;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.BindView;
import butterknife.ButterKnife;

import io.xsor.countrypicker.Country;
import io.xsor.countrypicker.CountryPickerCallback;
import io.xsor.countrypicker.CountryPickerDialog;

public class MainActivity extends AppCompatActivity {

    private CountryPickerDialog countryPickerDialog;
    private String scrollToCountry;

    @BindView(R.id.ivFlag)
    ImageView ivFlag;

    @BindView(R.id.tvCountry)
    TextView tvCountry;

    @OnClick(R.id.btShowDialog)
    void onShowDialogClick(View v) {
        countryPickerDialog.withScrollToCountry(null);
        countryPickerDialog.show();
    }

    @OnClick(R.id.btShowDialogScroll)
    void onShowDialogScrollClick(View v) {
        countryPickerDialog.withScrollToCountry(scrollToCountry);
        countryPickerDialog.show();
    }

    @OnClick({R.id.rbNormal, R.id.rbRound})
    void onRadioButtonsClicked(RadioButton rb) {

        boolean checked = rb.isChecked();

        if(checked) {
            switch (rb.getId()) {
                case R.id.rbNormal:
                    countryPickerDialog.withRoundFlags(false);
                    break;
                case R.id.rbRound:
                    countryPickerDialog.withRoundFlags(true);
                    break;
            }
        }
    }

    @OnCheckedChanged(R.id.cbDialingCode)
    void onDialingCodeCheckboxChange(CheckBox cb) {
        countryPickerDialog.withDialingCode(cb.isChecked());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountryPickerCallback callback = new CountryPickerCallback() {
            @Override
            public void onCountrySelected(Country country, int flagResId) {
                ivFlag.setImageResource(flagResId);
                tvCountry.setText(String.format(getString(R.string.selected_country),
                        country.getCountryName(),
                        country.getIsoCode(),
                        country.getDialingCodeInt()));
                scrollToCountry = country.getIsoCode();
                countryPickerDialog.dismiss();
            }
        };

        countryPickerDialog = new CountryPickerDialog(this, callback)
                                    .withDialingCode(false)
                                    .withRoundFlags(true);
        ButterKnife.bind(this);

    }
}
