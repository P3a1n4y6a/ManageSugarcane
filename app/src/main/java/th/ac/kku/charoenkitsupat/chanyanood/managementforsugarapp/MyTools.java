package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Panya on 30/5/2560.
 */

class MyTools {

    void setCardColor(View itemView, String status) {
        if (status.equalsIgnoreCase("complete")) {
            itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorComplete));
        } else if (status.equalsIgnoreCase("prepare")) {
            itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrepare));
        } else {
            itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorNonWork));
        }
    }

    void setSymbolQC(ImageView itemView, String status) {
        if (status.equalsIgnoreCase("PASS") || status.equalsIgnoreCase("ผ่าน")) {
            itemView.setImageResource(R.drawable.ic_checked);
        } else if (status.equalsIgnoreCase("DECLINE") || status.equalsIgnoreCase("ไม่ผ่าน")) {
            itemView.setImageResource(R.drawable.ic_cancel);
        } else {
            itemView.setImageResource(R.drawable.ic_dash);
        }
    }

    /*public String loadPreferencesAuthorization(Context context) {
        AppCompatActivity activity = (AppCompatActivity) context;
        SharedPreferences preferences = activity.getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String authen = preferences.getString("authen", "Not found");
        return authen;
    }

    public String loadPreferencesCookie(Context context) {
        AppCompatActivity activity = (AppCompatActivity) context;
        SharedPreferences preferences = activity.getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String cookie = preferences.getString("cookie", "Not found");
        String[] separatedData = cookie.split(";");
        cookie = separatedData[0];
        return cookie;
    }*/

    void addFragment(View v, Fragment currentFragment, String tag, String namedStack) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame
                        , currentFragment //Add new fragment to view group
                        , tag) //Bind named tag
                .addToBackStack(namedStack) //Keep a fragment into stack and can call it later by using namedStack
                .commit(); //Start fragment
    }

    void removeFragment(View v, String tag) {
        //Find new Instance of fragment by using tag and findFragmentByTag();
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment currentFragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if(currentFragment != null) { //Not found required fragment, it return null value
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment) //delete exist fragment from view group
                    .commit(); //Start fragment
            //Fragment that is added and removed must be the same fragment
        }
    }

    void replaceFragment(View v, Fragment f) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, f); //Define required instruction
        transaction.addToBackStack(null); //FragmentTransaction keeps fragments in back stack
        transaction.commit(); //Start fragment
    }
    // getFragmentManager().popBackStack(); for back individual fragment
}
