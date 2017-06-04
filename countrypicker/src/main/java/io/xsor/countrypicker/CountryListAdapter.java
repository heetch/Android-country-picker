package io.xsor.countrypicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

  private List<Country> countries;
  private boolean showDialingCode = false;
  private boolean roundFlags = false;
  private CountryPickerCallback mCallback;

  public CountryListAdapter(List<Country> countries) {
    this.countries = countries;
  }

  public void setCallback(CountryPickerCallback mCallback) {
    this.mCallback = mCallback;
  }

  public void withRoundFlags(boolean roundFlags) {
    this.roundFlags = roundFlags;
  }

  public void withDialingCode(boolean showDialingCode) {
    this.showDialingCode = showDialingCode;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.
        from(parent.getContext()).
        inflate(R.layout.item_country, parent, false);

    return new ViewHolder(itemView, this);
  }

  @Override
  public void onBindViewHolder(ViewHolder item, int position) {
    Country country = countries.get(position);

    Context c = item.icon.getContext();

    String itemText = new Locale(Locale.getDefault().getLanguage(), country.getIsoCode())
        .getDisplayCountry();

    if (showDialingCode) {
      itemText += String
          .format(c.getString(R.string.format_dialing_code), country.getDialingCode());
    }

    item.name.setText(itemText);

    int flagResId = CountryPickerUtils
        .getFlagResId(c, country.getIsoCode().toLowerCase(Locale.ENGLISH));

    if (roundFlags) {
      item.icon.setImageBitmap(CountryPickerUtils.getCircleCroppedBitmap(c, flagResId));
    } else {
      item.icon.setImageResource(flagResId);
    }
  }

  @Override
  public int getItemCount() {
    return countries.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView name;
    final ImageView icon;
    final CountryListAdapter adapter;

    ViewHolder(View itemView, CountryListAdapter adapter) {
      super(itemView);
      name = (TextView) itemView.findViewById(R.id.name);
      icon = (ImageView) itemView.findViewById(R.id.icon);

      this.adapter = adapter;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      Country country = adapter.countries.get(getAdapterPosition());
      adapter.mCallback
          .onCountrySelected(country, CountryPickerUtils.getFlagResId(view.getContext(),
              country.getIsoCode().toLowerCase(Locale.ENGLISH)));
    }
  }
}
