package com.heetch.countrypicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private final Context mContext;
    private static final String TAG = CountryListAdapter.class.getSimpleName();
    private List<Country> countries;
    private boolean showDialingCode;
    public OnItemClickListener onItemClickListener;

    public CountryListAdapter(Context context, List<Country> countries, boolean showDialingCode,
                              OnItemClickListener onItemClickListener) {
        mContext = context;
        this.countries = countries;
        this.showDialingCode = showDialingCode;
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.name.setText(String.format("%s%s", new Locale(mContext.getResources()
                .getConfiguration().locale.getLanguage(),
                country.getIsoCode()).getDisplayCountry(), showDialingCode ?
                " (+" + country.getDialingCode() + ")" : ""));
        String drawableName = country.getIsoCode().toLowerCase(Locale.ENGLISH) + "_flag";
        holder.icon.setImageResource(Utils.getMipmapResId(holder.icon.getContext(), drawableName));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition(), countries.get(getAdapterPosition()));
        }
    }

    interface OnItemClickListener{
        void onItemClick(int position, Country country);
    }
}
