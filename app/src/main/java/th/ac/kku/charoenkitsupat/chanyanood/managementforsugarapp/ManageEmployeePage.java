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
 * Created by Panya on 23/7/2560.
 */

public class ManageEmployeePage extends Fragment {
    View manageView;
    ViewPager viewPager;
    TabLayout tabLayout;
    int current_page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manageView = inflater.inflate(R.layout.manage_employee_page, container, false); // Bind this fragment with the layout
        ((NavigationMain)getActivity()).getSupportActionBar().show(); //Show action bar in this fragment
        ((NavigationMain) getActivity()).enableViews(false);
        getActivity().setTitle(R.string.manage_employee);
        setHasOptionsMenu(true);

        try {
            current_page = getArguments().getInt("FromCreateEmp");
        } catch (NullPointerException e) {
            current_page = 0;
        }

        viewPager = (ViewPager) manageView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) manageView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return manageView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        menu.clear();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SurveyManagement(), getActivity().getString(R.string.survey_emp));
        adapter.addFragment(new CutterManagement(), getActivity().getString(R.string.cutter_emp));
        adapter.addFragment(new TractorManagement(), getActivity().getString(R.string.tractor_emp));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(current_page);
    }
}
