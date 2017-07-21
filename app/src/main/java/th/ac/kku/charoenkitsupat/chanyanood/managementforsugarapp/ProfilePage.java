package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Panya on 25/5/2560.
 */

public class ProfilePage extends Fragment {

    View profileView;
    TextView fullname_txt, email_txt, company_name_txt, department_txt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.profile_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        getActivity().setTitle(R.string.profile_page);
        setHasOptionsMenu(true);
        initUserProfile();
        return profileView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        menu.clear();
    }

    public void initUserProfile(){
        String data = loadPreferencesUser();
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            fullname_txt = (TextView) profileView.findViewById(R.id.fullname);
            email_txt = (TextView) profileView.findViewById(R.id.emailData);
            company_name_txt = (TextView) profileView.findViewById(R.id.companyName);
            department_txt = (TextView) profileView.findViewById(R.id.departStatus);

            fullname_txt.setText(jsonObject.getString("full_name"));
            email_txt.setText(jsonObject.getString("email"));
            company_name_txt.setText(jsonObject.getString("company_name")); // not match
            department_txt.setText(jsonObject.getString("department"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadPreferencesUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("user", "Not found");
        return data;
    }
}
