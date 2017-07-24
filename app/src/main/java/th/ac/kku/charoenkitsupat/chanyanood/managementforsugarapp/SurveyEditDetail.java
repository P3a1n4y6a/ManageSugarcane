package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Panya on 30/5/2560.
 */

public class SurveyEditDetail extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View editSurveyView;
    Spinner qcSpin, gradeSpin;
    Button saveBtn, changeBtn; //action
    String[] sendToApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        editSurveyView = inflater.inflate(R.layout.survey_edit_detail, container, false);
        ((NavigationMain) getActivity()).getSupportActionBar().show();

        initSpinner();
        initButton();
        initPreviousData(); //Get all data to show on edit text

        return editSurveyView;
    }

    public void initSpinner() {
        qcSpin = (Spinner) editSurveyView.findViewById(R.id.qcSpin);
        gradeSpin = (Spinner) editSurveyView.findViewById(R.id.gradeSpin);

        String[] qc = getResources().getStringArray(R.array.real_qc);
        ArrayAdapter<String> adapterQc = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, qc);
        qcSpin.setAdapter(adapterQc);

        String[] grade = getResources().getStringArray(R.array.grade);
        ArrayAdapter<String> adapterGrade = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, grade);
        gradeSpin.setAdapter(adapterGrade);

        qcSpin.setOnItemSelectedListener(this);
        gradeSpin.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.qcSpin:

                break;
            case R.id.gradeSpin:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void initButton() {
        saveBtn = (Button) editSurveyView.findViewById(R.id.saveButton);
        changeBtn = (Button) editSurveyView.findViewById(R.id.setDate);

        saveBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    public void initPreviousData() {
        String[] message = getArguments().getStringArray("FromSurveyMoreDetail");
        TextView textData;
        int[] viewId = {R.id.idData, R.id.outputData, R.id.dateData, R.id.qcSpin, R.id.nameData,
                R.id.addressData, R.id.subDtName, R.id.districtName, R.id.provinceName,
                R.id.explorerName, R.id.explorerTelData, R.id.gradeSpin};
        int i = 0;
        while (i < viewId.length) {
            if (i == 3) {
                qcSpin.setSelection(2);
            } else if (i == 11) {
                gradeSpin.setSelection(getIndex(gradeSpin, message[i]));
            }else {
                textData = (TextView) editSurveyView.findViewById(viewId[i]);
                textData.setText(message[i]);
            }
            i++;
        }
    }

    private int getIndex(Spinner spinner, String defaultValue){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(defaultValue)){
                index = i;
            }
        }
        return index;
    }
}
