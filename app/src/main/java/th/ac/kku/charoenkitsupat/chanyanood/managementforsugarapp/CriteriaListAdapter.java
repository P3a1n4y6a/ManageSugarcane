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

public class CriteriaListAdapter extends BaseAdapter {
    private Context context;
    private String[] data;

    public CriteriaListAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
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
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.question = (TextView) convertView.findViewById(R.id.criteriaLabel);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.question.setText(data[position]);
            return convertView;
    }

    public class ViewHolder {
        TextView question;
    }
}
