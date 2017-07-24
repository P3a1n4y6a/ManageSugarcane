package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Panya on 30/5/2560.
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView surveyDate;

    public DateDialog(View view) {
        surveyDate = (TextView) view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String data = year + "-" + (month + 1) + "-" + dayOfMonth;
        surveyDate.setText(data);
    }
}
