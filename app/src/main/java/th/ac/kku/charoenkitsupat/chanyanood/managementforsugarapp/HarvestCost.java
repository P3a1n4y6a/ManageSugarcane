package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Panya on 31/5/2560.
 */

public class HarvestCost extends Fragment {
    View harvestCostView;
    GridView grid;
    MyTools tools = new MyTools();
    int[] totals = {10, 32, 210000, 320000}; //Query
    String[] noData = {"50000220-001", "50000221-002", "50000230-001"}; //Query

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        harvestCostView = inflater.inflate(R.layout.cost_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        getActivity().setTitle(R.string.cost_page);
        initLayout();
        return harvestCostView;
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

    public void initLayout() {
        //Harvest cost summary
        TextView title = (TextView)harvestCostView.findViewById(R.id.title);
        title.setText(getActivity().getString(R.string.harvest_summary));

        //From date
        TextView firstDate = (TextView)harvestCostView.findViewById(R.id.firstDate);
        firstDate.setText("23/05/2017");
        //To date
        TextView lastDate = (TextView)harvestCostView.findViewById(R.id.lastDate);
        lastDate.setText("24/05/2017");
        initGridLayout();
        initListView();
        initButton();
    }

    public void initGridLayout() {
        GridCost adapter = new GridCost(getActivity(), totals, getStringXML());
        grid = (GridView)harvestCostView.findViewById(R.id.gridView);
        grid.setAdapter(adapter);
    }

    public void initListView() {
        //now you must initialize your list view
        String noLabel = getResources().getString(R.string.cut_no);
        ListView listView = (ListView)harvestCostView.findViewById(R.id.listView);
        listView.setAdapter(new CostListAdapter(getActivity(), noData, noLabel));
    }

    public void initButton() {
        Button compareBtn = (Button) harvestCostView.findViewById(R.id.compareButton);
        compareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.replaceFragment(v, new CompareHarvestCost());
            }
        });
    }
}
