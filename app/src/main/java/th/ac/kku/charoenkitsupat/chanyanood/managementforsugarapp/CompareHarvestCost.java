package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panya on 2/6/2560.
 */

public class CompareHarvestCost extends android.support.v4.app.Fragment {
    View compareView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    String[][] data = {{"9", "10", "10%"}, {"120", "100", "16.67%"}, {"140000000000", "10000", "28.57%"}
            , {"1400000000", "10000", "28.57%"}}; //{Real, Estimate, Error}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        compareView = inflater.inflate(R.layout.compare_page, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);
        getActivity().setTitle(R.string.harvest_cost);
        setHasOptionsMenu(true);
        initRecycler();
        return compareView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        menu.clear();
    }

    public void initRecycler() {
        recyclerView = (RecyclerView) compareView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String[] title = {getResources().getString(R.string.weight_unit),
                            getResources().getString(R.string.fuel_content_unit),
                            getResources().getString(R.string.fuel_cost_unit),
                            getResources().getString(R.string.repair_unit)};
        adapter = new CompareRecyclerAdapter(data, title);
        recyclerView.setAdapter(adapter);
    }
}
