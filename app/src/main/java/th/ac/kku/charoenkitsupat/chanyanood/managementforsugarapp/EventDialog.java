package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Panya on 15/6/2560.
 */

public class EventDialog extends DialogFragment implements View.OnClickListener {
    EditText eventEdit, contentEdit, costEdit;
    ImageView close;
    Button saveBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Define dialog size
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);

        View view = inflater.inflate(R.layout.dialog_edit_event, container);
        eventEdit = (EditText) view.findViewById(R.id.eventData);
        contentEdit = (EditText) view.findViewById(R.id.contentData);
        costEdit = (EditText) view.findViewById(R.id.costData);

        close = (ImageView) view.findViewById(R.id.close_ic);
        saveBtn = (Button) view.findViewById(R.id.saveButton);

        // Show soft keyboard automatically
        eventEdit.requestFocus();

        close.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_ic) {
            // exit
            this.dismiss();
        } else if(v.getId() == R.id.saveButton) {
            // save and exit
        }
    }
}
