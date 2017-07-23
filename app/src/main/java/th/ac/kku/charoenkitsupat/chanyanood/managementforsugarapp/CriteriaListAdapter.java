package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Panya on 28/5/2560.
 */

public class CriteriaListAdapter extends RecyclerView.Adapter<CriteriaListAdapter.ViewHolder> {
    private List<String> data;
    private String[] criteria;

    public CriteriaListAdapter(List<String> data, String[] criteria) {
        this.data = data;
        this.criteria = criteria;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.criteria_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.question.setText(data.get(position));
        holder.answer.setText(criteria[position]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView answer;

        ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.question);
            answer = (TextView) itemView.findViewById(R.id.answer);
        }
    }
}
