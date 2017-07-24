package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Panya on 23/7/2560.
 */

public class CreateEmployeePage extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    View createAccView;
    Context context;
    TextInputLayout staff_id, staff_password;
    RadioGroup departChoice;
    Button saveBtn, cancelBtn;
    String department = "survey" , id, password;
    MyTools tools = new MyTools();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createAccView = inflater.inflate(R.layout.create_employee_page, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        getActivity().setTitle(R.string.add_emp_page);
        setHasOptionsMenu(true);
        initListener();
        return createAccView;
    }

    public boolean isEmptyIdPassword(){
        staff_id = (TextInputLayout) createAccView.findViewById(R.id.idLabel);
        staff_password = (TextInputLayout) createAccView.findViewById(R.id.passwordLabel);

        id = staff_id.getEditText().getText().toString();
        password = staff_password.getEditText().getText().toString();

        Log.d("EmployeeAccount", id + " : " + password);

        staff_id.setErrorEnabled(false);
        staff_password.setErrorEnabled(false);

        if (id.matches("") || password.matches("")) {
            staff_id.setError(getResources().getString(R.string.error_blank));
            staff_password.setError(getResources().getString(R.string.error_blank));
            return false;
        } else {
            return true;
        }
    }

    public void initListener() {
        departChoice = (RadioGroup) createAccView.findViewById(R.id.radioGroup);
        saveBtn = (Button) createAccView.findViewById(R.id.saveBtn);
        cancelBtn = (Button) createAccView.findViewById(R.id.cancelBtn);

        departChoice.setOnCheckedChangeListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.surveyRadio:
                department = "survey";
                break;
            case R.id.cutterRadio:
                department = "cutter";
                break;
            case R.id.tractorRadio:
                department = "tractor";
                break;
        }
        Log.d("EmployeeAccount", department);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                if (isEmptyIdPassword()) { // User puts their id and password
                    new OkHttpHandler(context, createAccView).execute();
                    Log.d("EmployeeAccount", "save" + " no : " + loadPreferencesNo());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_suggest),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancelBtn:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request;
        Response response;
        String CONTRACTOR_NO = loadPreferencesNo();
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        private Context context;
        private View rootView;

        public OkHttpHandler(Context context, View rootView){
            this.context = context;
            this.rootView = rootView;
        }

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/contractor/create_user_account?contractor_no="+
                    CONTRACTOR_NO + "&id=" + id + "&password=" + password + "&department=" + department;

            RequestBody formBody = new FormBody.Builder()
                    .add("contractor_no", CONTRACTOR_NO)
                    .add("id", id)
                    .add("password", password)
                    .add("department", department)
                    .build();

            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .post(formBody)
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
        protected void onPostExecute(String account) {
            super.onPostExecute(account);
            Log.d("EmployeeAccount", account);
            if (!account.contains("[]")) {
                try {
                    JSONObject jsonObject = new JSONObject(account);
                    String result = jsonObject.getString("result");
                    if (result.equalsIgnoreCase("successful")) {

                        // Back to previous view pager.
                        int position;
                        if(department.contains("survey")){
                            position = 0;
                        } else if (department.contains("cutter")) {
                            position = 1;
                        } else {
                            position = 2;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt("FromCreateEmp", position);
                        ManageEmployeePage manageEmployeePage = new ManageEmployeePage();
                        manageEmployeePage.setArguments(bundle);
                        tools.replaceFragment(rootView, manageEmployeePage);

                    } else {
                        Toast.makeText(getActivity(), result,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String loadPreferencesNo() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("contractor_no", "Not found");
        return data;
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
