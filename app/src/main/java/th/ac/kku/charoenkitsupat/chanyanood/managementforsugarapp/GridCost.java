package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Panya on 26/5/2560.
 */

public class GridCost extends BaseAdapter {
    private Context context;
    private String[][] labels;
    private int[] totals;

    public GridCost(Context context, int[] totals, String[][] labels) {
        this.context = context;
        this.totals = totals;
        this.labels = labels;
    }

    @Override
    public int getCount() {
        return totals.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridCost.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new GridCost.ViewHolder();
            convertView = inflater.inflate(R.layout.label_item, null); //More detail in layout resource
            holder.measureLabel = (TextView)convertView.findViewById(R.id.titleLabel);
            holder.unitLabel = (TextView)convertView.findViewById(R.id.unitLabel);
            holder.sumCost = (TextView)convertView.findViewById(R.id.totalLabel);
            convertView.setTag(holder);
        } else {
            holder = (GridCost.ViewHolder) convertView.getTag();
        }
        holder.measureLabel.setText(labels[position][0]);
        holder.unitLabel.setText(labels[position][1]);
        holder.sumCost.setText(totals[position] + "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertView.setBackground(context.getDrawable(R.drawable.green_light_label));}
        return convertView;
    }

    public class ViewHolder {
        TextView measureLabel;
        TextView unitLabel;
        TextView sumCost;
    }
}
