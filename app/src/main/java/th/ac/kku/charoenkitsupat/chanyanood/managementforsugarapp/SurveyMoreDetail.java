package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 27/5/2560.
 */

public class SurveyMoreDetail extends Fragment {
    private View surveyMoreDetailView;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    MyTools tools = new MyTools();
    ImageView qcData;
    TextView gradeData, plantId, nameData, plantAddressData, outputData, dateData, explorerName,
            explorerTel, title;
    String[] sendToEdit = new String[12];
    String latitude, longitude;
    double lat, lng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surveyMoreDetailView = inflater.inflate(R.layout.survey_more_detail, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        ((NavigationMain) getActivity()).enableViews(true);
        getActivity().setTitle(R.string.survey_queue_page);

        qcData = (ImageView) surveyMoreDetailView.findViewById(R.id.qcData);
        gradeData = (TextView) surveyMoreDetailView.findViewById(R.id.gradeData);
        plantId = (TextView) surveyMoreDetailView.findViewById(R.id.plantId);
        nameData = (TextView) surveyMoreDetailView.findViewById(R.id.nameData);
        plantAddressData = (TextView) surveyMoreDetailView.findViewById(R.id.plantAddressData);
        outputData = (TextView) surveyMoreDetailView.findViewById(R.id.outputData);
        dateData = (TextView) surveyMoreDetailView.findViewById(R.id.dateData);
        explorerName = (TextView) surveyMoreDetailView.findViewById(R.id.explorerName);
        explorerTel = (TextView) surveyMoreDetailView.findViewById(R.id.explorerTel);

        new OkHttpHandler().execute();
        new OkHttpHandler2().execute();
        initFloatingBtn();
        return surveyMoreDetailView;
    }

    public void initFloatingBtn() {
        FloatingActionButton editBtn = (FloatingActionButton) surveyMoreDetailView.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("FromSurveyMoreDetail", sendToEdit);
                SurveyEditDetail surveyEditDetail = new SurveyEditDetail();
                surveyEditDetail.setArguments(bundle);
                tools.replaceFragment(v, surveyEditDetail);
            }
        });
    }

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            String PLANT_ID = getArguments().getString("PlantRecyclerSendPlantId"); // Plant id from survey queue
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
        protected void onPostExecute(String SurveyData) {
            super.onPostExecute(SurveyData);
            Log.d("SurveyDetail", SurveyData);

            try {
                if (SurveyData != null) {
                    // Survey data from api Get survey profile
                    JSONArray jsonArray = new JSONArray(SurveyData); //Convert string to JSON
                    JSONObject jsonObject = jsonArray.getJSONObject(0); // get JSON at index
                    sendToEdit[0] = jsonObject.getString("PLANT_ID");
                    sendToEdit[1] = jsonObject.getString("est_total_cane_weight");
                    sendToEdit[2] = jsonObject.getString("can_start_harvest_date");
                    sendToEdit[3] = jsonObject.getString("calculate_qc");

                    // Plant data from api Get plant profile
                    int position = getArguments().getInt("PlantRecyclerSendIndex"); //Position of selected card
                    String plantData = loadPreferencesPlant();
                    JSONArray jsonArray2 = new JSONArray(plantData);
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(position);
                    sendToEdit[4] = jsonObject2.getString("farmer_name");
                    sendToEdit[5] = jsonObject2.getString("address");
                    sendToEdit[6] = jsonObject2.getString("sub_district");
                    sendToEdit[7] = jsonObject2.getString("district");
                    sendToEdit[8] = jsonObject2.getString("province");
                    sendToEdit[9] = jsonObject2.getString("explorer_name");
                    sendToEdit[10] = jsonObject2.getString("explorer_chief_tel");

                    tools.setSymbolQC(qcData, sendToEdit[3]); // Image
                    plantId.setText(sendToEdit[0]);
                    nameData.setText(sendToEdit[4]);
                    String fullAddress = sendToEdit[5] + " " + sendToEdit[6] + " " + sendToEdit[7] + " " + sendToEdit[8];
                    plantAddressData.setText(fullAddress);
                    outputData.setText(sendToEdit[1]);
                    dateData.setText(sendToEdit[2]);
                    explorerName.setText(sendToEdit[9]);
                    explorerTel.setText(sendToEdit[10]);

                    //Pin location on map
                    latitude = jsonObject.getString("lat_center");
                    longitude = jsonObject.getString("lng_center");
                    specifyLocationOnMap(latitude, longitude);
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    private String loadPreferencesPlant() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("plant", "Not found");
        return data;
    }

    public void specifyLocationOnMap(String latitude, String longitude) {
        FragmentManager fragMan = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragMan.findFragmentById(R.id.map_frag);

        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng location = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(location)
                        .title(getResources().getString(R.string.title_pin)));
                //CameraPosition cameraPos = CameraPosition.builder().target
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
            }
        });
    }

    private class OkHttpHandler2 extends AsyncTask<Object, Object, String> {
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            String PLANT_ID = getArguments().getString("PlantRecyclerSendPlantId"); // Plant id from survey queue
            final String URL = "http://188.166.191.60/api/v1/plant/contractor_get_plant_grade?PLANT_ID=" + PLANT_ID;

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
        protected void onPostExecute(String criteriaData) {
            super.onPostExecute(criteriaData);
            Log.d("CriteriaDetail", criteriaData);
            if (!criteriaData.contains("[]")) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(criteriaData);//Convert string to JSON
                    JSONObject jsonObject = jsonArray.getJSONObject(0); // get JSON at index
                    String answer = jsonObject.getString("all_input_value");
                    String cal_grade = jsonObject.getString("calculate_grade");

                    String[] answers = answer.split(",");
                    initEvaluationCriteria(answers);
                    changeGradeToChar(cal_grade);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                title = (TextView) surveyMoreDetailView.findViewById(R.id.title);
                title.setText(getResources().getString(R.string.no_evaluation));
            }
        }
    }

    public List<String> getStringXML() {
        // This is only criteria without evaluation in each criteria
        List<String> criteria = Arrays.asList(getResources().getStringArray(R.array.criteria));
        return criteria;
    }

    public void initEvaluationCriteria(String[] answers) {
        RecyclerView recyclerView = (RecyclerView) surveyMoreDetailView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        List<String> question = getStringXML();
        RecyclerView.Adapter adapter = new CriteriaListAdapter(question, answers);
        recyclerView.setAdapter(adapter);
    }

    public void changeGradeToChar(String cal_grade) {
        float gradeNum = 0;
        try {
            gradeNum = Float.parseFloat(cal_grade);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (gradeNum >= 2.5 && gradeNum <= 3.0) {
            gradeData.setText("A");
            sendToEdit[11] = "A";
        } else if (gradeNum >= 2.0 && gradeNum < 2.5) {
            gradeData.setText("B");
            sendToEdit[11] = "B";
        } else {
            gradeData.setText("C");
            sendToEdit[11] = "C";
        }
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
}
