package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Panya on 26/5/2560.
 */

public class CompareRecyclerAdapter extends RecyclerView.Adapter<CompareRecyclerAdapter.ViewHolder> {
    private String[][] data;
    private String[] title;

    CompareRecyclerAdapter(String[][] data, String[] title) {
        this.data = data;
        this.title = title;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleItem;
        TextView realItem;
        TextView estimateItem;
        TextView errorItem;

        ViewHolder(final View itemView) {
            super(itemView);
            titleItem = (TextView)itemView.findViewById(R.id.label);
            realItem = (TextView)itemView.findViewById(R.id.realData);
            estimateItem = (TextView)itemView.findViewById(R.id.estimateData);
            errorItem = (TextView)itemView.findViewById(R.id.errorData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.compare_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.titleItem.setText(title[i]);
        viewHolder.realItem.setText(data[i][0]);
        viewHolder.estimateItem.setText(data[i][1]);
        viewHolder.errorItem.setText(data[i][2]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
