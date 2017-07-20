package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

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
        String[] message = getArguments().getStringArray("FromFarmerMoreDetail");
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
        switch (v.getId()) {
            case R.id.saveButton:

                break;
        }
    }
}
