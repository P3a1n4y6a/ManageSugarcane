package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Panya on 25/5/2560.
 */

public class LoginPage extends Fragment implements View.OnClickListener {
    View loginView;
    Button loginBtn;
    EditText email, password;
    String email_str, password_str;

    private static final String app_pref = "app_pref";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginView = inflater.inflate(R.layout.login_page, container, false);
        ((NavigationMain)getActivity()).getSupportActionBar().hide();

        email = (EditText) loginView.findViewById(R.id.userMail);
        password = (EditText) loginView.findViewById(R.id.userKey);

        loginBtn = (Button) loginView.findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        return loginView;
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    @Override
    public void onClick(View v) {
        //((NavigationMain)getActivity()).loadFragment("DashboardPage");
        email_str = email.getText().toString();
        password_str = password.getText().toString();
        new OkHttpHandler().execute();
    }

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, List<String>> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected List<String> doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v1/authenticate";
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email_str)
                    .addFormDataPart("password", password_str)
                    .build();

            // Request type with POST
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();
            List return_list = new ArrayList<String>();
            try {
                Response response = client.newCall(request).execute();

                Map<String, List<String>> headerList = response.headers().toMultimap();
                return_list.add(headerList.get("Set-Cookie").get(0)); // token
                return_list.add(headerList.get("Set-Cookie").get(1)); // laravel-session
                return_list.add(response.body().string());
                return return_list;
                //return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result.get(2)); // get data from response
                    String response_txt = "";
                    if(jsonObject.has("error")) {

                        response_txt = jsonObject.getString("error");
                        Toast.makeText(getActivity(), response_txt, Toast.LENGTH_LONG).show();
                    }else{
                        String authen = jsonObject.getString("token");

                        //Create cookie
                        String session, token;
                        token = result.get(0); // full token
                        session = result.get(1); // full laravel
                        token = token.substring(0, token.indexOf(';') + 1);
                        session = session.substring(0, session.indexOf(';'));

                        String cookie = token + session;

                        //Share Preferences key: token, cookie = token + session
                        saveToSharedPrefs(authen, cookie);

                        new OkHttpHandler2().execute();

                        response_txt = "Authentication successful.";
                        Toast.makeText(getActivity(), response_txt,Toast.LENGTH_LONG).show();
                        Log.d("LoginPage", cookie);
                        ((NavigationMain)getActivity()).loadFragment("DashboardPage");
                    }

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
        return cookie;
    }

    // Call library to connect api with http request protocol.
    private class OkHttpHandler2 extends AsyncTask<Object, Object, String> {
        private Context context;
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/authenticate/profile";

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
            Log.d("ProfilePage", data);
            saveToSharedPrefs(data);
        }
    }

    public void saveToSharedPrefs(String data) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user", data); //Have not convert to json yet
        editor.commit();
    }

    public void saveToSharedPrefs(String authen, String cookie){
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("authen", authen);
        editor.putString("cookie", cookie);
        editor.commit();
    }
}
