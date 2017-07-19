package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Panya on 28/5/2560.
 */

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private String[][] events;

    public EventListAdapter(Context context, String[][] events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.length;
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
            convertView = inflater.inflate(R.layout.event_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.eventName);
            holder.qualify = (TextView) convertView.findViewById(R.id.qualify);
            holder.cost = (TextView) convertView.findViewById(R.id.cost);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(events[position][0]);
        holder.qualify.setText("(" + events[position][1] + ")");
        holder.cost.setText(events[position][2] + " บาท");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Event list", "" + position);
                showEditDialog();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView name;
        TextView qualify;
        TextView cost;
    }

    void showEditDialog() {
        Activity activity = (Activity) context;
        FragmentManager fragmentManager = activity.getFragmentManager();
        EventDialog editDialog = new EventDialog();
        editDialog.show(fragmentManager, "fragment_edit_name");
    }
}
