package com.heetch.countrypicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class CountryListAdapterRV extends RecyclerView.Adapter<CountryListAdapterRV.ViewHolder> {

    private final Context mContext;
    private List<Country> countries;
    private boolean showDialingCode;
    private boolean roundFlags;
    private Callback mCallback;

    public interface Callback {
        void onItemClicked(int index);
    }

    public CountryListAdapterRV(Context context, List<Country> countries, boolean showDialingCode) {
        mContext = context;
        this.countries = countries;
        this.showDialingCode = showDialingCode;
    }

    public CountryListAdapterRV(Context context, List<Country> countries, boolean showDialingCode, boolean roundFlags) {
        this(context,countries,showDialingCode);
        this.roundFlags = roundFlags;
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
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


        item.name.setText(String.format(mContext.getString(R.string.country_code),
                new Locale(Locale.getDefault().getLanguage(), country.getIsoCode()).getDisplayCountry(),
                showDialingCode ? country.getDialingCode() : null)
        );

        int flagResId = Utils.getMipmapResId(mContext, country.getIsoCode().toLowerCase(Locale.ENGLISH) + "_flag");

        if(roundFlags) {
            item.icon.setImageBitmap(Utils.getCircleCroppedBitmap(mContext,flagResId));
        } else {
            item.icon.setImageResource(flagResId);
        }
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView name;
        final ImageView icon;
        final CountryListAdapterRV adapter;

        ViewHolder(View itemView, CountryListAdapterRV adapter) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            icon = (ImageView) itemView.findViewById(R.id.icon);

            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            adapter.mCallback.onItemClicked(getAdapterPosition());
        }
    }
}
