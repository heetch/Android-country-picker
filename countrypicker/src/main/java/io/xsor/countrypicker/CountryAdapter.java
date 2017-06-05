package io.xsor.countrypicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import io.xsor.countrypicker.CountryPickerDialog.Listener;
import java.util.Comparator;
import java.util.Locale;

public class CountryAdapter extends SortedListAdapter<Country> {

  private boolean showDialingCode = false;
  private boolean roundFlags = false;
  private Listener listener;

  public CountryAdapter(Context context, Comparator<Country> comparator,
      Listener listener, boolean roundFlags, boolean showDialingCode) {
    super(context, Country.class, comparator);
    this.listener = listener;
    this.showDialingCode = showDialingCode;
    this.roundFlags = roundFlags;
  }

  @NonNull
  @Override
  protected ViewHolder<? extends Country> onCreateViewHolder(@NonNull LayoutInflater layoutInflater,
      @NonNull ViewGroup viewGroup, int i) {
    View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.item_country, viewGroup, false);

    return new ExampleViewHolder(itemView);
  }

  private class ExampleViewHolder extends SortedListAdapter.ViewHolder<Country> implements View
      .OnClickListener {

    private final TextView name;
    private final ImageView icon;
    private int flagResId;
    private Country country;

    ExampleViewHolder(View itemView) {

      super(itemView);

      itemView.setOnClickListener(this);
      name = (TextView) itemView.findViewById(R.id.name);
      icon = (ImageView) itemView.findViewById(R.id.icon);
    }

    @Override
    protected void performBind(@NonNull Country item) {
      Context c = icon.getContext();
      country = item;
      String itemText = new Locale(Locale.getDefault().getLanguage(), item.getIsoCode())
          .getDisplayCountry();

      if (showDialingCode) {
        itemText += " " + String
            .format(c.getString(R.string.format_dialing_code), item.getDialingCode());
      }

      name.setText(itemText);

      flagResId = Utils
          .getFlagResId(c, item.getIsoCode().toLowerCase(Locale.ENGLISH));

      if (roundFlags) {
        //icon.setImageDrawable(Utils.getRoundedDrawable(c, flagResId));
        icon.setImageBitmap(Utils.getCircleCroppedBitmap(c, flagResId));
      } else {
        icon.setImageResource(flagResId);
      }
    }

    @Override
    public void onClick(View view) {
      listener.onCountrySelected(country, flagResId);
    }
  }
}
