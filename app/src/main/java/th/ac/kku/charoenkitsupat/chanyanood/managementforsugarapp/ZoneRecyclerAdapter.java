package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Panya on 26/5/2560.
 */

public class ZoneRecyclerAdapter extends RecyclerView.Adapter<ZoneRecyclerAdapter.ViewHolder> implements Filterable {
    private ArrayList<ZoneModel> arrayList;
    private ArrayList<ZoneModel> mFilteredList;
    private SupportMapFragment mapFragment;
    private SearchView search;

    ZoneRecyclerAdapter(ArrayList<ZoneModel> arrayList, SupportMapFragment mapFragment, SearchView search) {
        this.arrayList = arrayList;
        this.mFilteredList = arrayList;
        this.mapFragment = mapFragment;
        this.search = search;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    results.count = 0;
                    results.values = null;
                } else {
                    ArrayList<ZoneModel> filteredZone = new ArrayList<>();
                    for (ZoneModel model : arrayList) {
                        if (model.getZoneId().toLowerCase().contains(charString)
                                || model.getCompanyName().toLowerCase().contains(charString))
                            filteredZone.add(model);
                    }
                    results.count = filteredZone.size();
                    results.values = filteredZone;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ZoneModel>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemZone;
        TextView itemCpn;

        ViewHolder(final View itemView) {
            super(itemView);
            if (mFilteredList != null) {
                itemZone = (TextView) itemView.findViewById(R.id.zoneData);
                itemCpn = (TextView) itemView.findViewById(R.id.companyData);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng position = new LatLng(mFilteredList.get(getAdapterPosition()).getLat(),
                                        mFilteredList.get(getAdapterPosition()).getLng());
                                CameraUpdate center = CameraUpdateFactory.newLatLng(position);
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                                googleMap.moveCamera(center);
                                googleMap.animateCamera(zoom);

                                // Hide list after press to select
                                search.setQuery("", false);
                                search.clearFocus();
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.zone_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (mFilteredList != null && i < mFilteredList.size()) {
            viewHolder.itemZone.setText(mFilteredList.get(i).getZoneId());
            viewHolder.itemCpn.setText(mFilteredList.get(i).getCompanyName());
        }
    }

    @Override
    public int getItemCount() {
        return (mFilteredList == null) ? 0 : mFilteredList.size();
    }
}
