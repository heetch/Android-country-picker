package com.heetch.countrypicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> implements Filterable {

    private final Context mContext;
    private static final String TAG = CountryListAdapter.class.getSimpleName();
    private ArrayList<Country> countries;
    private boolean showDialingCode;
    public OnItemClickListener onItemClickListener;
    private List<Country> countryListFiltered;

    public CountryListAdapter(Context context, List<Country> countries, boolean showDialingCode,
                              OnItemClickListener onItemClickListener) {
        mContext = context;
        this.countries = (ArrayList<Country>) countries;
        countryListFiltered = countries;
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
        Country country = countryListFiltered.get(position);
        holder.name.setText(country.getCountryName());
        String drawableName = country.getIsoCode().toLowerCase(Locale.ENGLISH) + "_flag";
        holder.icon.setImageResource(Utils.getMipmapResId(holder.icon.getContext(), drawableName));
    }

    @Override
    public int getItemCount() {
        return countryListFiltered.size();
    }

    public void update(List<Country> filterList) {
        countries.clear();
        countries.addAll(filterList);
        notifyDataSetChanged();

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    countryListFiltered = countries;
                } else {
                    List<Country> filteredList = new ArrayList<>();
                    for (Country row : countries) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCountryName().toLowerCase()
                                .contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    countryListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = countryListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                countryListFiltered = (ArrayList<Country>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}
