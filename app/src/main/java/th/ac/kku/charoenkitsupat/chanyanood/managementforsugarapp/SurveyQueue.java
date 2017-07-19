package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panya on 27/5/2560.
 */

public class SurveyQueue extends Fragment implements SearchView.OnQueryTextListener {
    View surveyQueueView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    //{status, plant id, date, qc, output(ton)}
    private String[][] data = {{"complete", "5555-001", "30/1/17", "ผ่าน", "10"}, {"complete", "5555-002", "31/3/17", "ไม่ผ่าน", "11"}
            , {"complete", "5556-001", "12/4/17", "ไม่ผ่าน", "9"}, {"prepare", "5556-002", "12/4/17", "ไม่มีข้อมูล", "8"}};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surveyQueueView = inflater.inflate(R.layout.sort_query_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        getActivity().setTitle(R.string.survey_queue_page);
        setHasOptionsMenu(true);
        initRecycler();

        return surveyQueueView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void initRecycler(){
        recyclerView = (RecyclerView) surveyQueueView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlantRecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
