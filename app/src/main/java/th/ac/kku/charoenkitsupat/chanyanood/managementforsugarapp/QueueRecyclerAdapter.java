package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Panya on 26/5/2560.
 */

class QueueRecyclerAdapter extends RecyclerView.Adapter<QueueRecyclerAdapter.ViewHolder> {
    //ArrayList<CutterModel> cutterList;
    private static String id;
    private String title;
    MyTools tools = new MyTools();

    /*QueueRecyclerAdapter(String title, ArrayList<CutterModel> cutterList) {
        this.title = title;
        this.cutterList = cutterList;
    }*/

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemCut;
        TextView itemTitle;

        ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.cutIdLabel);
            itemCut = (TextView) itemView.findViewById(R.id.cutIdData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int position = getAdapterPosition();
                    id = data[position];
                    if(title.equalsIgnoreCase("หมายเลขรถตัด:"))
                        tools.replaceFragment(itemView, new TotalTractor());
                    else if (title.equalsIgnoreCase("หมายเลขรถบรรทุก:"))
                        tools.replaceFragment(itemView, new TotalHarvest());*/
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.queue_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(title);
        //viewHolder.itemCut.setText(data[i]);
    }

    @Override
    public int getItemCount() {
        return 0;//cutterList.size();
    }

    public static String getId() {
        return id;
    }
}
