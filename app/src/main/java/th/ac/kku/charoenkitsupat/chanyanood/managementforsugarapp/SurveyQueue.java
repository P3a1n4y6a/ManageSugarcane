package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 27/5/2560.
 */

public class SurveyQueue extends Fragment implements SearchView.OnQueryTextListener {
    View surveyQueueView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<PlantModel> plantList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surveyQueueView = inflater.inflate(R.layout.sort_query_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        getActivity().setTitle(R.string.survey_queue_page);
        setHasOptionsMenu(true);
        new OkHttpHandler().execute();
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

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        private Context context;
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/plant/PlantList";

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
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("PlantList", data);
            //saveToSharedPrefs(data);

            try {
                if (data != null) {
                    JSONArray jsonArray = new JSONArray(data); //Convert string to JSON
                    plantList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index
                        plantList.add(new PlantModel(jsonObject.getString("PLANT_ID"),
                                jsonObject.getString("start_survey_date"),
                                jsonObject.getString("survey_status"),
                                "Na"));//Info of each farmer
                    }
                    initRecycler(); //Create multiple cards
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initRecycler(){
        recyclerView = (RecyclerView) surveyQueueView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlantRecyclerAdapter(plantList);
        recyclerView.setAdapter(adapter);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
