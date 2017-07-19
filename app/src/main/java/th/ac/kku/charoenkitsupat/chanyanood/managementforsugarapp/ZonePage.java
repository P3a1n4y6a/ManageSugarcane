package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Panya on 30/5/2560.
 */

public class ZonePage extends Fragment implements SearchView.OnQueryTextListener, GoogleMap.OnMarkerClickListener {
    View zoneMoreView;
    SupportMapFragment mapFragment;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchView searchView;
    ZoneRecyclerAdapter adapter;
    ArrayList<Marker> markerList = new ArrayList<Marker>();
    ArrayList<Information> plantMarker = new ArrayList<Information>();
    ArrayList<ZoneModel> zoneInfoList;
    private ArrayList<Information> plantInfoList;
    private String address = "140/130 Mittraphap Road, Nei Muang, Sub disstrict";
    private double lat, lng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        zoneMoreView = inflater.inflate(R.layout.zone_more_detail, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        ((NavigationMain) getActivity()).enableViews(false); // Hamberger
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.zone_page);

        new OkHttpHandler().execute();
        return zoneMoreView;
    }

    public void initRecycler() {
        recyclerView = (RecyclerView) zoneMoreView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ZoneRecyclerAdapter(zoneInfoList, mapFragment, searchView);
        //recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Actually, we get all data from API in JSON format. This is only assumed data
    public void loadData() {
        // Orange marker is plant area, Actually we get 'near plant' around each zone
        plantInfoList = new ArrayList<>();
        plantInfoList.add(new Information(16.581718, 103.090231, "1"));
        plantInfoList.add(new Information(16.504967, 103.016158, "1"));
        plantInfoList.add(new Information(16.543438, 103.010245, "1"));
        plantInfoList.add(new Information(17.357940, 102.965906, "2"));
        plantInfoList.add(new Information(17.213548, 103.032460, "2"));
        plantInfoList.add(new Information(17.112880, 103.014758, "2"));
        plantInfoList.add(new Information(16.071846, 103.846475, "3"));
        plantInfoList.add(new Information(15.963281, 103.863739, "3"));
        plantInfoList.add(new Information(16.040524, 103.892552, "3"));
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
    private class OkHttpHandler extends AsyncTask<Object, Object, String> {
        String cookie = loadPreferencesCookie(); // cookie is token + laravel-session header
        String authen = loadPreferencesAuthorization(); // token is header

        @Override
        protected String doInBackground(Object... params) {
            final String URL = "http://188.166.191.60/api/v1/zone";

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
            //Log.d("ZonePage", data);
            try {
                if (data != null) {
                    JSONArray jsonArray = new JSONArray(data); //Convert string to JSON
                    zoneInfoList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i); // get JSON at index

                        String lat_str, lng_str;
                        lat_str = jsonObject.getString("zone_lat");
                        lng_str = jsonObject.getString("zone_lng");

                        try {
                            lat = Double.parseDouble(lat_str);
                            lng = Double.parseDouble(lng_str);
                        } catch (NumberFormatException e) {
                            // Nothing
                        }
                        zoneInfoList.add(new ZoneModel(
                                jsonObject.getString("CONTRACTOR_NO"),
                                lat, lng, jsonObject.getString("ZONE_ID"),
                                jsonObject.getString("cane_company")));
                            /*Log.d("ZonePage ", "contractor_no : " + jsonObject.getString("CONTRACTOR_NO")
                            + "latitude : " + lat + "longitude : " + lng + "company : " + jsonObject.getString("cane_company"));*/
                    }
                    //Toast.makeText(getActivity(), "Done! there are " + zoneInfoList.size() + " data", Toast.LENGTH_LONG).show();
                    stickyZone();
                    initRecycler(); //Create multiple cards
                } else {
                    Toast.makeText(getActivity(), "Is zone data null?", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stickyZone() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frag);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (zoneInfoList != null) {
                    for (int i = 0; i < zoneInfoList.size(); i++) {
                        createMarkerZone(googleMap, zoneInfoList.get(i).getLat()
                                , zoneInfoList.get(i).getLng()
                                , zoneInfoList.get(i).getZoneId() + " " + zoneInfoList.get(i).getCompanyName()
                                , zoneInfoList.get(i).getZoneId());
                    }
                } else {
                    Toast.makeText(getActivity(), "Is zoneInfoList.size null?", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void stickyPlantation() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (int i = 0; i < plantMarker.size(); i++) {
                    /*createMarkerPlant(googleMap, plantMarker.get(i).getLatitude()
                            , plantMarker.get(i).getLongitude()
                            , address, plantMarker.get(i).getZoneId());*/
                }
                plantMarker.clear();
            }
        });
    }

    protected GoogleMap createMarkerZone(GoogleMap googleMap, double latitude, double longitude
            , String title, String key) {
        LatLng position = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).snippet(key).title(title));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
        //googleMap.setOnMarkerClickListener(this);
        return googleMap;
    }

    protected GoogleMap createMarkerPlant(GoogleMap googleMap, double latitude, double longitude
            , String title, String key) {
        LatLng position = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).snippet(key).title(title));
        markerList.add(marker);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 8));
        return googleMap;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null) {
            adapter.getFilter().filter(newText);
            recyclerView.setAdapter(adapter);
        }
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Sent getSnippet() to server, calculate distance then get result to array in android app.
        String selectedZone = String.valueOf(marker.getSnippet());
        for (int i = 0; i < zoneInfoList.size(); i++) {
            if (selectedZone.equalsIgnoreCase(zoneInfoList.get(i).getZoneId())) {
                ZoneModel zoneInfo = zoneInfoList.get(i);
                if (zoneInfo.isOnMap()) {
                    // Remove plant markers
                    for (int j = 0; j < markerList.size(); j++) {
                        if (selectedZone.equalsIgnoreCase(markerList.get(j).getSnippet())) {
                            markerList.get(j).remove(); // Remove marker from map
                            // Have a big bug, markerList is added more and more, can't solve
                        }
                    }
                    zoneInfo.setOnMap(false);
                } else {
                    // Add plant markers
                    for (int j = 0; j < plantInfoList.size(); j++) {
                        if (selectedZone.equalsIgnoreCase(plantInfoList.get(j).getZoneId())) {
                            plantMarker.add(plantInfoList.get(j));
                        }
                    }
                    //stickyPlantation();
                    zoneInfo.setOnMap(true);
                }
            }
        }
        return true;
    }

}

