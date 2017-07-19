package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Panya on 27/5/2560.
 */

public class SurveyMoreDetail extends Fragment implements View.OnClickListener {
    private View surveyMoreDetailView, insertView;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    LinearLayout parentLayout;
    TextView plantId, date, qc, output;
    private Button showBtn, editBtn; // action

    MyTools tools = new MyTools();
    private double lat = 16.4718442, lng = 102.825422;
    private String here = "Farmer plant's location";
    int[] viewId = {R.id.plantData, R.id.nameData, R.id.plantAddressData, R.id.dateData
            , R.id.outputData, R.id.explorerName};
    boolean hide = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surveyMoreDetailView = inflater.inflate(R.layout.survey_more_detail, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();
        ((NavigationMain)getActivity()).enableViews(true);
        getActivity().setTitle(R.string.survey_queue_page);

        specifyLocationOnMap();
        initPreviousData();
        initButton();
        return surveyMoreDetailView;
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

    public String[] getStringXML(){
        // This is only criteria without evaluation in each criteria
        String[] criteria;
        return criteria = getResources().getStringArray(R.array.criteria);
    }

    public void specifyLocationOnMap(){
        FragmentManager fragMan = getChildFragmentManager();
        mapFragment = (SupportMapFragment)fragMan.findFragmentById(R.id.map_frag);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng khonkaen = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(khonkaen).title(here));
                //CameraPosition cameraPos = CameraPosition.builder().target
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(khonkaen, 14));
            }
        });
    }

    public void initPreviousData(){
        plantId = (TextView) surveyMoreDetailView.findViewById(R.id.plantData);
        plantId.setText(PlantRecyclerAdapter.getPlantId());
    }

    public void initButton(){
        showBtn = (Button) surveyMoreDetailView.findViewById(R.id.showButton);

        parentLayout = (LinearLayout) surveyMoreDetailView.findViewById(R.id.blankLayout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        insertView = inflater.inflate(R.layout.survey_show_detail, null);

        // Get value in survey queue page to survey more detail page
        date = (TextView) insertView.findViewById(R.id.dateData);
        date.setText(PlantRecyclerAdapter.getDate());
        qc = (TextView) insertView.findViewById(R.id.qcData);
        qc.setText(PlantRecyclerAdapter.getQC());
        output = (TextView) insertView.findViewById(R.id.outputData);
        output.setText(PlantRecyclerAdapter.getOutput());

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
                    parentLayout.addView(insertView, parentLayout.getChildCount());
                    //now you must initialize your list view
                    ListView listView = (ListView)insertView.findViewById(R.id.listView); //survey_show_detail
                    listView.setAdapter(new CriteriaListAdapter(getActivity(), getStringXML()));

                    editBtn = (Button) surveyMoreDetailView.findViewById(R.id.editButton);
                    editBtn.setOnClickListener(this);
                } else {
                    hide = true;
                    showBtn.setText(getResources().getString(R.string.show_button));
                    parentLayout.removeView(insertView);
                } break;
            case R.id.editButton: //Go to edit all data
                Bundle bundle = new Bundle();
                bundle.putStringArray("FromMoreDetail", tools.getAllData(viewId, surveyMoreDetailView));
                SurveyEditDetail surveyEditDetail = new SurveyEditDetail();
                surveyEditDetail.setArguments(bundle);
                tools.replaceFragment(v, surveyEditDetail);
                break;
        }
    }

}
