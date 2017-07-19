package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Panya on 26/5/2560.
 */

public class GridDashboard extends BaseAdapter {
    private Context context;
    private String[] title; //Button name
    private int[] icId; //Image ID to call image from drawable resource

    public GridDashboard(Context context, String[] title, int[] icId) {
        this.context = context;
        this.icId = icId;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.length;
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
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.button_item, null); //More detail in layout resource
            holder.title = (TextView)convertView.findViewById(R.id.grid_text);
            holder.icon = (ImageView)convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(title[position]);
        holder.icon.setImageResource(icId[position]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertView.setBackground(context.getDrawable(R.drawable.white_btn));}
        return convertView;
    }

    public class ViewHolder {
        TextView title;
        ImageView icon;
    }
}
