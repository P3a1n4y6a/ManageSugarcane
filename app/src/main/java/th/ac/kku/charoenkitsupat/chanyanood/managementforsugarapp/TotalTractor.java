package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Panya on 29/5/2560.
 */

public class TotalTractor extends Fragment {
    View tractorView;
    GridView grid;
    int[] totals = {10, 32, 210000, 320000}; //Query

    String[][] events = {{"เติมน้ำมัน", "50", "5000"}
            , {"ซ่อมมอเตอร์", "", "1000"}, {"ชั่วโมงทำงาน", "9", ""}}; // {event name,quality, cost}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tractorView = inflater.inflate(R.layout.total_cost, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        ((NavigationMain) getActivity()).enableViews(true);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.survey_queue_page);

        GridCost adapter = new GridCost(getActivity(), totals, getStringXML());
        grid = (GridView) tractorView.findViewById(R.id.gridView);
        grid.setAdapter(adapter);

        initPreviousData();
        initList();
        return tractorView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public void initPreviousData() {
        TextView title = (TextView) tractorView.findViewById(R.id.title);
        title.setText(R.string.total_time);
        TextView id = (TextView) tractorView.findViewById(R.id.plantData);
        id.setText(QueueRecyclerAdapter.getId());
        TextView no = (TextView) tractorView.findViewById(R.id.cutNoData);
        no.setText(CostListAdapter.getNoOrder());
    }

    public String[][] getStringXML() {
        String[][] words = new String[4][2]; //{measure, unit}
        words[0][0] = getActivity().getResources().getString(R.string.work_time);
        words[0][1] = getActivity().getResources().getString(R.string.hour_unit);

        words[1][0] = getActivity().getResources().getString(R.string.fuel_content);
        words[1][1] = getActivity().getResources().getString(R.string.litre_unit);

        words[2][0] = getActivity().getResources().getString(R.string.repair);
        words[2][1] = getActivity().getResources().getString(R.string.baht_unit);

        words[3][0] = getActivity().getResources().getString(R.string.fuel_cost);
        words[3][1] = getActivity().getResources().getString(R.string.baht_unit);
        return words;
    }

    public void initList() {
        //now you must initialize your list view
        ListView listView = (ListView) tractorView.findViewById(R.id.listView);
        listView.setAdapter(new EventListAdapter(getActivity(), events));
    }
}
