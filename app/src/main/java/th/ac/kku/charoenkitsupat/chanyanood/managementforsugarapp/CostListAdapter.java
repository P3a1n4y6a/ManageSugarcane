package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Panya on 28/5/2560.
 */

class CostListAdapter extends BaseAdapter {
    private Context context;
    private String[] noData;
    private String label;
    MyTools tools = new MyTools();
    private static String noOrder;

    CostListAdapter(Context context, String[] noData, String label) {
        this.context = context;
        this.noData = noData;
        this.label = label;
    }

    @Override
    public int getCount() {
        return noData.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.queue_item, null);
            holder.label = (TextView) convertView.findViewById(R.id.cutIdLabel);
            holder.noData = (TextView) convertView.findViewById(R.id.cutIdData);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.label.setText(label);
        holder.noData.setText(noData[position]);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOrder = noData[position];
                if (label.equalsIgnoreCase("Tractor order no:")
                        || label.equalsIgnoreCase("หมายเลขลำดับรถบรรทุก:"))
                    tools.replaceFragment(v, new TotalTractor());
                else if (label.equalsIgnoreCase("Cutter order no:")
                        || label.equalsIgnoreCase("หมายเลขลำดับรถตัด:"))
                    tools.replaceFragment(v, new TotalHarvest());
            }
        });
        return convertView;
    }
    static String getNoOrder() {
        return noOrder;
    }

    public class ViewHolder {
        TextView label;
        TextView noData;
    }
}
