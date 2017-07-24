package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 23/7/2560.
 */

public class CutterManagement extends Fragment implements SearchView.OnQueryTextListener {
    View cutterEmpView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<EmployeeModel> cutterList;
    MyTools tools = new MyTools();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cutterEmpView = inflater.inflate(R.layout.query_add_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        //getActivity().setTitle(R.string.survey_queue_page);
        setHasOptionsMenu(true);
        new OkHttpHandler().execute();
        initFloatingBtn();
        return cutterEmpView;
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

    public void initFloatingBtn() {
        FloatingActionButton editBtn = (FloatingActionButton) cutterEmpView.findViewById(R.id.addBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tools.replaceFragment(v, new CreateEmployeePage());
            }
        });
    }

    public void initRecycler(ArrayList<EmployeeModel> arrayList){
        recyclerView = (RecyclerView) cutterEmpView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //String title = getResources().getString(R.string.cut_id);
        adapter = new EmployeeRecyclerAdapter("cutter", arrayList);
        recyclerView.setAdapter(adapter);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> {
        String CONTRACTOR_NO = loadPreferencesNo();
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/contractor/get_user_list?contractor_no="
                    + CONTRACTOR_NO + "&department=cutter";

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .header("Authorization", authen)
                    .addHeader("Cookie", cookie)
                    .build();

            try {
                // Call newCall() to connect server, return Call class then call execute()
                Response response = okHttpClient.newCall(request).execute(); // execute() returns Response
                // When finish sending and receiving data with server, check result
                if (response.isSuccessful()) {
                    return response.body().string(); // Read data
                } else {
                    return "Not Success - code : " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        * Use result from doInBackground()
        * */
        @Override
        protected void onPostExecute(String surveyAccount) {
            super.onPostExecute(surveyAccount);
            Log.d("CutterAccount", surveyAccount);
            if (surveyAccount != null) {
                try {
                    JSONObject jsonObject = new JSONObject(surveyAccount);
                    // Getting JSON Array node
                    JSONArray result = jsonObject.getJSONArray("result");
                    cutterList = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        cutterList.add(new EmployeeModel(item.getString("CUTTER_ID")
                                , item.getString("PASSWORD")));
                    }
                    initRecycler(cutterList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String loadPreferencesAuthorization() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String authen = preferences.getString("authen", "Not found");
        return authen;
    }

    private String loadPreferencesCookie() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String cookie = preferences.getString("cookie", "Not found");
        String[] separatedData = cookie.split(";");
        cookie = separatedData[0];
        return cookie;
    }

    private String loadPreferencesNo() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("contractor_no", "Not found");
        return data;
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
