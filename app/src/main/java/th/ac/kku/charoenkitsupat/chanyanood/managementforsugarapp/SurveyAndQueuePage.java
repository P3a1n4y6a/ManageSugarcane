package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panya on 25/5/2560.
 */

public class SurveyAndQueuePage extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    View surveyAndQueueView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surveyAndQueueView = inflater.inflate(R.layout.survey_queue_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(false);
        getActivity().setTitle(R.string.survey_queue_page);

        viewPager = (ViewPager) surveyAndQueueView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) surveyAndQueueView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return surveyAndQueueView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SurveyQueue(), getActivity().getString(R.string.survey_tap));
        adapter.addFragment(new HarvestQueue(), getActivity().getString(R.string.harvest_tap));
        adapter.addFragment(new TractorQueue(), getActivity().getString(R.string.tractor_tap));
        viewPager.setAdapter(adapter);
    }
}
