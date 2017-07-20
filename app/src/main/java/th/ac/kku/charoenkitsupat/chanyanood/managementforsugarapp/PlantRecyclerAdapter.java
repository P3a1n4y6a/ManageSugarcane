package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Panya on 26/5/2560.
 */

public class PlantRecyclerAdapter extends RecyclerView.Adapter<PlantRecyclerAdapter.ViewHolder> {
    private ArrayList<PlantModel> plantList;
    private MyTools tools = new MyTools();

    PlantRecyclerAdapter(ArrayList<PlantModel> plantList) {
        this.plantList = plantList;
        checkList(plantList);
    }

    void checkList(ArrayList<PlantModel> plantList){
        for(int i = 0; i < plantList.size(); i++) {
            Log.d("PlantRe", plantList.get(i).getId());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView itemPlant;
        TextView itemDate;
        TextView itemQC;
        TextView itemOutput;

        ViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            itemPlant = (TextView) itemView.findViewById(R.id.plantData);
            itemDate = (TextView) itemView.findViewById(R.id.dateData);
            itemQC = (TextView) itemView.findViewById(R.id.qcData);
            itemOutput = (TextView) itemView.findViewById(R.id.outputData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Bundle bundle = new Bundle();
                    bundle.putInt("FromPlantRecycler", position);
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
        tools.setCardColor(viewHolder.cardView, "no_queue");
        viewHolder.itemPlant.setText(plantList.get(i).getId());
        viewHolder.itemDate.setText(plantList.get(i).getStart_survey_date());
        viewHolder.itemQC.setText(plantList.get(i).getSurvey_status());
        viewHolder.itemOutput.setText(plantList.get(i).getOutput());
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }
}
