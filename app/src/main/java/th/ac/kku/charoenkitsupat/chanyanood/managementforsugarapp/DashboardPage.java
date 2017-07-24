package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by Panya on 25/5/2560.
 */


public class DashboardPage extends Fragment {
    View dashView;
    GridView grid;
    GridDashboard adapter;
    Context context;

    int[] icId = {R.drawable.ic_color_farmer, R.drawable.ic_color_survey, R.drawable.ic_color_zone,
            R.drawable.ic_color_cost, R.drawable.ic_color_team};

    public DashboardPage() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dashView = inflater.inflate(R.layout.dashboard_page, container, false); // Bind this fragment with the layout
        ((NavigationMain)getActivity()).getSupportActionBar().show(); //Show action bar in this fragment
        ((NavigationMain) getActivity()).enableViews(false);
        getActivity().setTitle(R.string.dash_page);
        setHasOptionsMenu(true);
        initCustomGrid(); //Create grid by use custom

        return dashView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        menu.clear();
    }

    public String[] getStringXML(){
        String[] title = new String[5];
        title[0] = getResources().getString(R.string.farmer_page);
        title[1] = getResources().getString(R.string.survey_queue_page);
        title[2] = getResources().getString(R.string.zone_page);
        title[3] = getResources().getString(R.string.cost_page);
        title[4] = getResources().getString(R.string.manage_employee);
        return title;
    }

    public void initCustomGrid() {
        grid = (GridView)dashView.findViewById(R.id.gridView);
        adapter = new GridDashboard(getActivity(), getStringXML(), icId); //Holder a custom layout
        grid.setAdapter(adapter); //Bind grid with adapter
        //Control action of buttons inside the grid
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: ((NavigationMain)getActivity()).loadFragment("FarmerInfoPage");
                        break;
                    case 1: ((NavigationMain)getActivity()).loadFragment("SurveyAndQueuePage");
                        break;
                    case 2: ((NavigationMain)getActivity()).loadFragment("ZonePage");
                        break;
                    case 3: ((NavigationMain)getActivity()).loadFragment("CostIntoPage");
                        break;
                    case 4: ((NavigationMain)getActivity()).loadFragment("ManagementPage");
                        break;
                }
            }
        });
    }
}
