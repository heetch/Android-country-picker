package io.xsor.countrypickersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.xsor.countrypicker.Country;
import io.xsor.countrypicker.Listener;
import io.xsor.countrypicker.Dialog;

public class MainActivity extends AppCompatActivity {

  private Dialog countryPickerDialog;
  private String scrollToCountry;
  private boolean roundFlags = true;
  private boolean dialingCodes;

  @BindView(R.id.ivFlag)
  ImageView ivFlag;
  @BindView(R.id.tvCountry)
  TextView tvCountry;

  Listener callback = new Listener() {
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

  @OnClick(R.id.btShowDialog)
  void onShowDialogClick(View v) {
    makeDialog();
    countryPickerDialog.setScrollToCountry(null);
    countryPickerDialog.show();
  }

  @OnClick(R.id.btShowDialogScroll)
  void onShowDialogScrollClick(View v) {
    makeDialog();
    countryPickerDialog.setScrollToCountry(scrollToCountry);
    countryPickerDialog.show();
  }

  @OnClick({R.id.rbNormal, R.id.rbRound})
  void onRadioButtonsClicked(RadioButton rb) {

    boolean checked = rb.isChecked();

    if (checked) {
      switch (rb.getId()) {
        case R.id.rbNormal:
          roundFlags = false;
          break;
        case R.id.rbRound:
          roundFlags = true;
          break;
      }
    }
  }

  @OnCheckedChanged(R.id.cbDialingCode)
  void onDialingCodeCheckboxChange(CheckBox cb) {
    dialingCodes = cb.isChecked();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    makeDialog();

  }

  public void makeDialog() {
    countryPickerDialog = null;
    countryPickerDialog = new Dialog(this, roundFlags, dialingCodes, callback);
  }
}
