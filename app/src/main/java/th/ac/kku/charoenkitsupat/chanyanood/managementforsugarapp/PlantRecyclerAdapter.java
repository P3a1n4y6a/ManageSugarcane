package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Panya on 26/5/2560.
 */

public class PlantRecyclerAdapter extends RecyclerView.Adapter<PlantRecyclerAdapter.ViewHolder> {
    private ArrayList<PlantModel> arrayList;
    private ArrayList<PlantModel> mFilteredList;
    private MyTools tools = new MyTools();

    PlantRecyclerAdapter(ArrayList<PlantModel> arrayList) {
        this.arrayList = arrayList;
        this.mFilteredList = arrayList;
        checkList(arrayList);
    }

    void checkList(ArrayList<PlantModel> plantList){
        for(int i = 0; i < plantList.size(); i++) {
            Log.d("PlantRe", plantList.get(i).getId());
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                ArrayList<PlantModel> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    mFilteredList = arrayList;
                } else {
                    for (PlantModel information : arrayList) {
                        if (information.getId().toLowerCase().contains(charString))
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
                mFilteredList = (ArrayList<PlantModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView itemPlant;
        TextView itemDate;
        ImageView itemQC;
        TextView itemOutput;

        ViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            itemPlant = (TextView) itemView.findViewById(R.id.plantData);
            itemDate = (TextView) itemView.findViewById(R.id.dateData);
            itemQC = (ImageView) itemView.findViewById(R.id.qcImage);
            itemOutput = (TextView) itemView.findViewById(R.id.outputData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Bundle bundle = new Bundle();
                    bundle.putString("PlantRecyclerSendPlantId", mFilteredList.get(position).getId());
                    bundle.putInt("PlantRecyclerSendIndex", position);
                    SurveyMoreDetail surveyMoreDetail = new SurveyMoreDetail();
                    surveyMoreDetail.setArguments(bundle);
                    tools.replaceFragment(itemView, surveyMoreDetail);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plant_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        tools.setCardColor(viewHolder.cardView, mFilteredList.get(i).getSurvey_status());
        viewHolder.itemPlant.setText(mFilteredList.get(i).getId());
        viewHolder.itemDate.setText(mFilteredList.get(i).getStart_survey_date());
        tools.setSymbolQC(viewHolder.itemQC, mFilteredList.get(i).getQc());
        viewHolder.itemOutput.setText(mFilteredList.get(i).getOutput());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
}
