package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 27/5/2560.
 */

public class SurveyMoreDetail extends Fragment implements View.OnClickListener {
    private View surveyMoreDetailView, insertView;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    LinearLayout parentLayout;
    TextView plantId, date, qc, output;
    private Button showBtn, editBtn; // action

    MyTools tools = new MyTools();
    private double lat = 16.4718442, lng = 102.825422;
    private String here = "Farmer plant's location";
    int[] viewId = {R.id.plantData, R.id.nameData, R.id.plantAddressData, R.id.dateData
            , R.id.outputData, R.id.explorerName};
    boolean hide = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surveyMoreDetailView = inflater.inflate(R.layout.survey_more_detail, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);
        getActivity().setTitle(R.string.survey_queue_page);
        specifyLocationOnMap();

        new OkHttpHandler().execute();
        //initButton();
        return surveyMoreDetailView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fragMan = getChildFragmentManager();
        mapFragment = (SupportMapFragment)fragMan.findFragmentById(R.id.map_frag);
        if (mapFragment != null){
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }

    /*public String[] getStringXML(){
        // This is only criteria without evaluation in each criteria
        String[] criteria;
        return criteria = getResources().getStringArray(R.array.criteria);
    }*/

    public void specifyLocationOnMap(){
        FragmentManager fragMan = getChildFragmentManager();
        mapFragment = (SupportMapFragment)fragMan.findFragmentById(R.id.map_frag);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng khonkaen = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(khonkaen).title(here));
                //CameraPosition cameraPos = CameraPosition.builder().target
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(khonkaen, 14));
            }
        });
    }

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        private Context context;
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            String PLANT_ID = getArguments().getString("FromPlantRecycler"); // Plant id from survey queue
            Log.d("survey id", PLANT_ID);
            final String URL = "http://188.166.191.60/api/v1/survey?PLANT_ID=" + PLANT_ID;

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
            Log.d("SurveyDetail", data);
            //saveToSharedPrefs(data);

            /*try {
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
            }*/
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
    /*public void initButton(){
        showBtn = (Button) surveyMoreDetailView.findViewById(R.id.showButton);

        parentLayout = (LinearLayout) surveyMoreDetailView.findViewById(R.id.blankLayout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        insertView = inflater.inflate(R.layout.survey_show_detail, null);

        // Get value in survey queue page to survey more detail page
        date = (TextView) insertView.findViewById(R.id.dateData);
        date.setText(PlantRecyclerAdapter.getDate());
        qc = (TextView) insertView.findViewById(R.id.qcData);
        qc.setText(PlantRecyclerAdapter.getQC());
        output = (TextView) insertView.findViewById(R.id.outputData);
        output.setText(PlantRecyclerAdapter.getOutput());

        showBtn.setOnClickListener(this);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showButton:
                if(hide) {
                    hide = false;
                    showBtn.setText(getResources().getString(R.string.hide_button));
                    // Add the new row before the add field button.
                    parentLayout.addView(insertView, parentLayout.getChildCount());
                    //now you must initialize your list view
                    ListView listView = (ListView)insertView.findViewById(R.id.listView); //survey_show_detail
                    //listView.setAdapter(new CriteriaListAdapter(getActivity(), getStringXML()));
                    editBtn = (Button) surveyMoreDetailView.findViewById(R.id.editButton);
                    editBtn.setOnClickListener(this);
                } else {
                    hide = true;
                    showBtn.setText(getResources().getString(R.string.show_button));
                    parentLayout.removeView(insertView);
                } break;
            case R.id.editButton: //Go to edit all data
                Bundle bundle = new Bundle();
                bundle.putStringArray("FromMoreDetail", tools.getAllData(viewId, surveyMoreDetailView));
                SurveyEditDetail surveyEditDetail = new SurveyEditDetail();
                surveyEditDetail.setArguments(bundle);
                tools.replaceFragment(v, surveyEditDetail);
                break;
        }
    }

}
