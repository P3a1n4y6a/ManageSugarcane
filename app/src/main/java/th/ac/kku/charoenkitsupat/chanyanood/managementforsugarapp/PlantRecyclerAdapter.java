package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Panya on 26/5/2560.
 */

public class PlantRecyclerAdapter extends RecyclerView.Adapter<PlantRecyclerAdapter.ViewHolder> {
    private String[][] data;
    private static String plant, date, qc, output;
    private MyTools tools = new MyTools();

    PlantRecyclerAdapter(String[][] data) {
        this.data = data;
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
                    plant = data[position][1];
                    date = data[position][2];
                    qc = data[position][3];
                    output = data[position][4];
                    tools.replaceFragment(itemView, new SurveyMoreDetail());
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
        tools.setCardColor(viewHolder.cardView, data[i][0]);
        viewHolder.itemPlant.setText(data[i][1]);
        viewHolder.itemDate.setText(data[i][2]);
        viewHolder.itemQC.setText(data[i][3]);
        viewHolder.itemOutput.setText(data[i][4]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static String getPlantId() {
        return plant;
    }

    public static String getDate() {
        return date;
    }

    public static String getQC() {
        return qc;
    }

    public static String getOutput() {
        return output;
    }
}
