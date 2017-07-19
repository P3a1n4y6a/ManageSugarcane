package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Panya on 26/5/2560.
 */

class FarmerRecyclerAdapter extends RecyclerView.Adapter<FarmerRecyclerAdapter.ViewHolder> {
    private ArrayList<FarmerModel> arrayList;
    private ArrayList<FarmerModel> mFilteredList;

    MyTools tools = new MyTools();

    FarmerRecyclerAdapter(ArrayList<FarmerModel> arrayList) {
        this.arrayList = arrayList;
        this.mFilteredList = arrayList;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                ArrayList<FarmerModel> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    mFilteredList = arrayList;
                } else {
                    for (FarmerModel information : arrayList) {
                        if (information.getFullname().toLowerCase().contains(charString)
                                || information.getId().toLowerCase().contains(charString))
                            filteredList.add(information);
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<FarmerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemId;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.nameData);
            itemId = (TextView) itemView.findViewById(R.id.idData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Bundle bundle = new Bundle();
                    bundle.putInt("FromFarmerRecycler", position);
                    FarmerMoreDetail farmerMoreDetail = new FarmerMoreDetail();
                    farmerMoreDetail.setArguments(bundle);

                    //Go to a farmer more detail layout when user selects one card in farmer information page
                    tools.replaceFragment(v, farmerMoreDetail);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.farmer_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(mFilteredList.get(i).getFullname());
        viewHolder.itemId.setText(mFilteredList.get(i).getId());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
}
