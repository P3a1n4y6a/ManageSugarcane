package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Panya on 30/5/2560.
 */

public class SurveyEditDetail extends Fragment implements View.OnClickListener {
    View editSurveyView;
    Button cancelBtn, saveBtn, changeBtn; //action

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        editSurveyView = inflater.inflate(R.layout.survey_edit_detail, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();

        initPreviousData(); //Get all data to show on edit text
        initButton();

        return editSurveyView;
    }

    public void initPreviousData() {
        String[] message = getArguments().getStringArray("FromMoreDetail");
        TextView textData;
        int[] viewId = {R.id.nameData, R.id.idData, R.id.plantAddressData, R.id.dateData
                , R.id.outputData, R.id.explorerName};
        for (int i = 0; i < viewId.length; i++) {
            textData = (TextView) editSurveyView.findViewById(viewId[i]);
            textData.setText(message[i]);
        }
    }

    public void initButton() {
        cancelBtn = (Button) editSurveyView.findViewById(R.id.cancelButton);
        saveBtn = (Button) editSurveyView.findViewById(R.id.saveButton);
        changeBtn = (Button) editSurveyView.findViewById(R.id.setDate);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                getFragmentManager().popBackStack();
                break;
            case R.id.saveButton:

                break;
            case R.id.setDate:
                TextView dateData = (TextView) editSurveyView.findViewById(R.id.dateData);
                DateDialog dialog = new DateDialog(dateData);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                dialog.show(fragmentTransaction, "DatePicker");
                break;
        }
    }
}
