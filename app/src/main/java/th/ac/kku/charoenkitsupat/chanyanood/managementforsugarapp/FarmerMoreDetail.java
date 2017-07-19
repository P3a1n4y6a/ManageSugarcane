package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 26/5/2560.
 */

public class FarmerMoreDetail extends Fragment implements View.OnClickListener {
    View farmerMoreView;
    View insertView; //Insert when user click a show detail button

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    LinearLayout parentLayout;
    MyTools tools = new MyTools();

    TextView id, name, home, tel, districtChief, districtChiefTel;
    Button showBtn, editBtn; // action
    String current_id; // selected farmer
    boolean hide = true;
    int[] viewId = {R.id.nameData, R.id.idData, R.id.homeData, R.id.farmerTelData
            , R.id.chiefNameData, R.id.chiefTelData};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        farmerMoreView = inflater.inflate(R.layout.farmer_more_detail, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);
        initPreviousData(); //Get name and id that user selects to show in farmer more detail
        initRecycler(); //Create cards about farmer's plantation

        setHasOptionsMenu(true);
        return farmerMoreView;
    }

    public void initPreviousData(){
        int position = getArguments().getInt("FromFarmerRecycler"); //Position of selected card

        //Get string to convert to json array so that get selected farmer
        String data = loadPreferencesFarmer();
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(position);

            name = (TextView) farmerMoreView.findViewById(R.id.nameData);
            id = (TextView) farmerMoreView.findViewById(R.id.idData);

            name.setText(jsonObject.getString("full_name"));
            id.setText(jsonObject.getString("FARMER_ID"));

            current_id = jsonObject.getString("FARMER_ID"); // Keep current selected farmer

            initButton(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadPreferencesFarmer() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("farmer", "Not found");
        return data;
    }

    public void initButton(JSONObject jsonObject){
        showBtn = (Button) farmerMoreView.findViewById(R.id.showButton);

        parentLayout = (LinearLayout) farmerMoreView.findViewById(R.id.blankLayout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        insertView = inflater.inflate(R.layout.farmer_show_detail, null);

        home = (TextView) insertView.findViewById(R.id.homeData);
        tel = (TextView) insertView.findViewById(R.id.farmerTelData);
        districtChief = (TextView) insertView.findViewById(R.id.chiefNameData);
        districtChiefTel = (TextView) insertView.findViewById(R.id.chiefTelData);

        try {
            String fullAddress = jsonObject.getString("address") + " " +
                    jsonObject.getString("sub_district") + " " + jsonObject.getString("district") + " " +
                    jsonObject.getString("province") + " " + jsonObject.getString("zip_code");

            home.setText(fullAddress);
            tel.setText("xxxxxxxxxx"); // Add in api
            districtChief.setText(jsonObject.getString("district_chief"));
            districtChiefTel.setText(jsonObject.getString("district_chief_tel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showButton:
                if(hide) {
                    hide = false;
                    showBtn.setText(getResources().getString(R.string.hide_button));
                    // Add the new row before the add field button.
                    parentLayout.addView(insertView, parentLayout.getChildCount() - 1);

                    editBtn = (Button) farmerMoreView.findViewById(R.id.editButton);
                    editBtn.setOnClickListener(this);
                } else {
                    hide = true;
                    showBtn.setText(getResources().getString(R.string.show_button));
                    parentLayout.removeView(insertView);
                } break;
            case R.id.editButton: //Go to edit all data
                Bundle bundle = new Bundle();
                bundle.putStringArray("FromMoreDetail", tools.getAllData(viewId, farmerMoreView));
                FarmerEditDetail farmerEditDetail = new FarmerEditDetail();
                farmerEditDetail.setArguments(bundle);
                tools.replaceFragment(v, farmerEditDetail);
                break;
        }
    }

    private String[][] data = {{"complete", "5555-001", "30/1/17", "ผ่าน", "10"}, {"complete", "5555-002", "31/3/17", "ไม่ผ่าน", "11"}
            , {"complete", "5556-001", "12/4/17", "ไม่ผ่าน", "9"}, {"prepare", "5556-002", "12/4/17", "ไม่มีข้อมูล", "8"}};

    public void initRecycler(){
        recyclerView = (RecyclerView) farmerMoreView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        new OkHttpHandler().execute();
        adapter = new PlantRecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        private Context context;
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/plant";

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
            Log.d("Plant", data);

            /*
            try {
                if (data != null) {
                    JSONArray jsonArray = new JSONArray(data); //Convert string to JSON
                    farmerInfoList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index
                        if(jsonObject.getString("FARMER_ID").equalsIgnoreCase(current_id)){
                            farmerInfoList.add(new FarmerModel(jsonObject.getString("FARMER_ID"),
                                jsonObject.getString("full_name")));//Info of each farmer
                        }
                    }
                    initRecycler(); //Create multiple cards
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    public void saveToSharedPrefs(String data) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("plant", data); //Have not convert to json yet
        editor.commit();
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
}
