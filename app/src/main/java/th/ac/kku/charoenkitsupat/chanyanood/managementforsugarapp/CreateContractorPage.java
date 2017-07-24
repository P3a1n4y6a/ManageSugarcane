package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panya on 23/7/2560.
 */

public class CreateContractorPage extends Fragment {
    View createView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createView = inflater.inflate(R.layout.create_contractor_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        //getActivity().setTitle(R.string.survey_queue_page);
        setHasOptionsMenu(true);
        //new OkHttpHandler().execute();
        return createView;
    }
}
