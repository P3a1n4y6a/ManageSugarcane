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
 * Created by Panya on 28/5/2560.
 */

public class TotalHarvest extends Fragment {
    View harvestView;
    GridView grid;
    int[] totals = {10, 32, 210000, 320000}; //Query

    String[][] events = {{"เติมน้ำมัน", "50", "5000"}
            , {"เติมยางรถ", "", "100"}, {"เก็บเกี่ยว", "100", ""}}; // {event name,quality, cost}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        harvestView = inflater.inflate(R.layout.total_cost, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);
        getActivity().setTitle(R.string.survey_queue_page);
        setHasOptionsMenu(true);
        GridCost adapter = new GridCost(getActivity(), totals, getStringXML());
        grid = (GridView)harvestView.findViewById(R.id.gridView);
        grid.setAdapter(adapter);

        initPreviousData();
        initList();
        return harvestView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        menu.clear();
    }

    public void initPreviousData(){
        TextView title = (TextView) harvestView.findViewById(R.id.title);
        title.setText(R.string.total_cut);
        TextView id = (TextView) harvestView.findViewById(R.id.plantData);
        id.setText(QueueRecyclerAdapter.getId());
        TextView no = (TextView) harvestView.findViewById(R.id.cutNoData);
        no.setText(CostListAdapter.getNoOrder());
    }

    public String[][] getStringXML() {
        String[][] words = new String[4][2]; //{measure, unit}
        words[0][0] = getActivity().getResources().getString(R.string.weight);
        words[0][1] = getActivity().getResources().getString(R.string.ton_unit);

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
        ListView listView = (ListView)harvestView.findViewById(R.id.listView);
        listView.setAdapter(new EventListAdapter(getActivity(), events));
    }
}
