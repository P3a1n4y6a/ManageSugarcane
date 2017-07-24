package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panya on 25/5/2560.
 */

public class CostInfoPage extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    View costInfoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        costInfoView = inflater.inflate(R.layout.cost_info_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(false);
        getActivity().setTitle(R.string.cost_page);
        setHasOptionsMenu(true);
        viewPager = (ViewPager) costInfoView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) costInfoView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return costInfoView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        menu.clear();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HarvestCost(), getActivity().getString(R.string.harvest_cost));
        adapter.addFragment(new TractorCost(), getActivity().getString(R.string.tractor_cost));
        viewPager.setAdapter(adapter);
    }
}
