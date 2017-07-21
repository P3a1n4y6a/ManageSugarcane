package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Panya on 27/5/2560.
 */

public class FarmerEditDetail extends Fragment implements View.OnClickListener{
    View editFarmerView;
    Button saveBtn; //action
    String[] message = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        editFarmerView = inflater.inflate(R.layout.farmer_edit_detail, container, false);
        getActivity().setTitle(R.string.edit_farmer_page);
        ((NavigationMain)getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);

        initPreviousData(); //Get all data to show on edit text
        initButton();

        return editFarmerView;
    }

    public void initPreviousData(){
        message = getArguments().getStringArray("FromFarmerMoreDetail");
        TextView textData;
        int[] viewId = {R.id.nameData, R.id.idData, R.id.addressData, R.id.subDtName, R.id.districtName,
                R.id.provinceName, R.id.zipCode, R.id.farmerTelData, R.id.chiefNameData, R.id.chiefTelData};
        for(int i = 0; i < viewId.length; i++){
            textData = (TextView) editFarmerView.findViewById(viewId[i]);
            textData.setText(message[i]);
        }
    }

    public void initButton() {
        saveBtn = (Button) editFarmerView.findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //new OkHttpHandler().execute();
    }

    // Call library to connect api with http request protocol.
    /*private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        private Context context;
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/farmer/edit";

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("old_email", message[11])
                    .addFormDataPart("FARMER_ID", password_str)
                    .build();

            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .put(requestBody)
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

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Log.d("EditFarmer", data);
            //saveToSharedPrefs(data);
        }
    }*/

    private String loadPreferencesAuthorization() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String authen = preferences.getString("authen", "Not found");
        return authen;
    }

    private String loadPreferencesCookie() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String cookie = preferences.getString("cookie", "Not found");
        return cookie;
    }
}
