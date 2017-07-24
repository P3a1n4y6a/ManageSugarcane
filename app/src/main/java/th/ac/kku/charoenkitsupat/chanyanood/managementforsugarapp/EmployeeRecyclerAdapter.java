package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

class EmployeeRecyclerAdapter extends RecyclerView.Adapter<EmployeeRecyclerAdapter.ViewHolder> {
    private Context ctx;
    private ArrayList<EmployeeModel> employeeList;
    private String depart, id, password, authen, cookie;
    MyTools tools = new MyTools();

    EmployeeRecyclerAdapter(String depart, ArrayList<EmployeeModel> employeeList) {
        if (employeeList == null) {
            throw new IllegalArgumentException("EmployeeList must not be null");
        }
        this.employeeList = employeeList;
        this.depart = depart;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemId;
        TextView itemPassword;
        ImageButton itemRemove;

        ViewHolder(final View itemView) {
            super(itemView);
            itemId = (TextView) itemView.findViewById(R.id.idData);
            itemPassword = (TextView) itemView.findViewById(R.id.passwordData);
            itemRemove = (ImageButton) itemView.findViewById(R.id.removeBtn);
            itemRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemRemove.getId()){
                int position = getAdapterPosition();
                id = employeeList.get(position).getId();
                password = employeeList.get(position).getPassword();

                // Call assert removing dialog

                authen = loadPreferencesAuthorization(v);
                cookie = loadPreferencesCookie(v);
                new OkHttpHandler().execute();
            }
        }
    }

    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        Context context;
        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/contractor/delete_user_account?id=" +
                    id + "&password=" + password + "&department=" + depart;

            OkHttpClient okHttpClient = new OkHttpClient();

            Request.Builder builder = new Request.Builder(); // Create request
            Request request = builder.url(URL)
                    .header("Authorization", authen)
                    .addHeader("Cookie", cookie)
                    .delete()
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
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            Log.d("DeleteAccount", message);

            try {
                JSONObject jsonObject = new JSONObject(message);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employ_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemId.setText(employeeList.get(i).getId());
        viewHolder.itemPassword.setText(employeeList.get(i).getPassword());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    private String loadPreferencesAuthorization(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        SharedPreferences preferences = activity.getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String authen = preferences.getString("authen", "Not found");
        return authen;
    }

    private String loadPreferencesCookie(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        SharedPreferences preferences = activity.getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String cookie = preferences.getString("cookie", "Not found");
        String[] separatedData = cookie.split(";");
        cookie = separatedData[0];
        return cookie;
    }
}
