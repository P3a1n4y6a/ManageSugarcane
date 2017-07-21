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
import android.widget.EditText;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Panya on 25/5/2560.
 */

public class LoginPage extends Fragment {
    View loginView;
    //Button loginBtn;
    LoadingButton loginBtn;
    EditText email, password;
    String email_str, password_str;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginView = inflater.inflate(R.layout.login_page, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().hide();

        email = (EditText) loginView.findViewById(R.id.userMail);
        password = (EditText) loginView.findViewById(R.id.userKey);

        loginBtn = (LoadingButton) loginView.findViewById(R.id.login);
        //while login failed, reset view to button with animation
        loginBtn.setResetAfterFailed(true);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_str = email.getText().toString();
                password_str = password.getText().toString();
                loginBtn.startLoading();
                new OkHttpHandler().execute();
            }
        });
        //loginBtn = (Button) loginView.findViewById(R.id.login);
        //loginBtn.setOnClickListener(this);
        return loginView;
    }

    /*@Override
    public void onClick(View v) {
        email_str = email.getText().toString();
        password_str = password.getText().toString();
        new OkHttpHandler().execute();
    }*/

    // Call library to connect api with http request protocol.
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody;
        Request request;
        Response response;

        @Override
        protected String doInBackground(Object... params) {
            String url = "http://188.166.191.60/api/v1/authenticate";
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email_str)
                    .addFormDataPart("password", password_str)
                    .build();

            // Request type with POST
            request = new Request.Builder()
                    .url(url)
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Login", result);
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result); // get data from response

                    if (jsonObject.has("error")) {
                        loginBtn.loadingFailed();
                        Toast.makeText(getActivity(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    } else {
                        loginBtn.loadingSuccessful();

                        //Create cookie
                        Map<String, List<String>> headerList = response.headers().toMultimap();
                        String token, session, authen, cookie;
                        token = headerList.get("Set-Cookie").get(0);// token
                        session = headerList.get("Set-Cookie").get(1);// laravel-session
                        token = token.substring(0, token.indexOf(';') + 1);
                        session = session.substring(0, session.indexOf(';'));
                        cookie = token + session;
                        authen = jsonObject.getString("token"); //

                        //Share Preferences key: token, cookie = token + session
                        saveToSharedPrefs(authen, cookie);

                        ((NavigationMain) getActivity()).loadFragment("DashboardPage");
                        new OkHttpHandler2().execute();
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
            final String URL = "http://188.166.191.60/api/v1/authenticate/get_account_profile?email=" + email_str;

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
            try {
                JSONArray jsonArray = new JSONArray(data);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Toast.makeText(getActivity(), getResources().getString(R.string.login_success) +
                        jsonObject.getString("full_name"), Toast.LENGTH_SHORT).show(); // Change to name
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToSharedPrefs(String data) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user", data); //Have not convert to json yet
        editor.commit();

    }

    public void saveToSharedPrefs(String authen, String cookie) {
        SharedPreferences pref = getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("authen", authen);
        editor.putString("cookie", cookie);
        editor.commit();
    }
}
