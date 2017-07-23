package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 26/5/2560.
 */

public class FarmerMoreDetail extends Fragment {
    View farmerMoreView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<PlantModel> plantList;
    MyTools tools = new MyTools();
    TextView id, name, home, tel, districtChief, districtChiefTel;
    String[] sendToEdit = new String[11];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        farmerMoreView = inflater.inflate(R.layout.farmer_more_detail, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);
        initFloatingBtn();
        initPreviousData(); //Get name and id that user selects to show in farmer more detail
        setHasOptionsMenu(true);
        new OkHttpHandler().execute();
        return farmerMoreView;
    }

    public void initFloatingBtn() {
        FloatingActionButton editBtn = (FloatingActionButton) farmerMoreView.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("FromFarmerMoreDetail", sendToEdit);
                FarmerEditDetail farmerEditDetail = new FarmerEditDetail();
                farmerEditDetail.setArguments(bundle);
                tools.replaceFragment(v, farmerEditDetail);
            }
        });
    }

    public void initPreviousData(){
        int position = getArguments().getInt("FromFarmerRecycler"); //Position of selected card

        //Get string to convert to json array so that get selected farmer
        String data = loadPreferencesFarmer();
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(position);

            id = (TextView) farmerMoreView.findViewById(R.id.idData);
            name = (TextView) farmerMoreView.findViewById(R.id.nameData);
            home = (TextView) farmerMoreView.findViewById(R.id.homeData);
            tel = (TextView) farmerMoreView.findViewById(R.id.farmerTelData);
            districtChief = (TextView) farmerMoreView.findViewById(R.id.chiefNameData);
            districtChiefTel = (TextView) farmerMoreView.findViewById(R.id.chiefTelData);

            sendToEdit[0] = jsonObject.getString("FARMER_ID");
            sendToEdit[1] = jsonObject.getString("full_name");
            sendToEdit[2] = jsonObject.getString("address");
            sendToEdit[3] = jsonObject.getString("sub_district");
            sendToEdit[4] = jsonObject.getString("district");
            sendToEdit[5] = jsonObject.getString("province");
            sendToEdit[6] = jsonObject.getString("zip_code");
            sendToEdit[7] = jsonObject.getString("tel_no");
            sendToEdit[8] = jsonObject.getString("district_chief_name");
            sendToEdit[9] = jsonObject.getString("district_chief_tel");
            sendToEdit[10] = jsonObject.getString("email");

            String fullAddress = sendToEdit[2] + " " + sendToEdit[3] + " " + sendToEdit[4] +
                    " " + sendToEdit[5] + " " + sendToEdit[6];

            id.setText(sendToEdit[0]);
            name.setText(sendToEdit[1]);
            home.setText(fullAddress);
            tel.setText(sendToEdit[7]);
            districtChief.setText(sendToEdit[8]);
            districtChiefTel.setText(sendToEdit[9]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadPreferencesFarmer() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("farmer", "Not found");
        return data;
    }

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        private Context context;
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header
        String CONTRACTOR_ID = loadPreferencesNo();

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/plant/contractor_get_plant_list?CONTRACTOR_NO=" +
                    CONTRACTOR_ID + "&FARMER_ID=" + sendToEdit[0];

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
            Log.d("PlantFarmer", data);
            saveToSharedPrefs(data);

            try {
                if (data != null) {
                    JSONArray jsonArray = new JSONArray(data); //Convert string to JSON
                    plantList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index
                        plantList.add(new PlantModel(jsonObject.getString("PLANT_ID"),
                                jsonObject.getString("start_survey_date"),
                                jsonObject.getString("survey_status"),
                                jsonObject.getString("est_total_cane_weight"),
                                jsonObject.getString("calculate_qc")));//Info of each farmer
                    }
                    initRecycler(); //Create multiple cards
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToSharedPrefs(String data) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("plant", data); //Have not convert to json yet
        editor.commit();
    }

    public void initRecycler(){
        recyclerView = (RecyclerView) farmerMoreView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

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

    private String loadPreferencesNo() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String no = preferences.getString("contractor_no", "Not found");
        return no;
    }
}
