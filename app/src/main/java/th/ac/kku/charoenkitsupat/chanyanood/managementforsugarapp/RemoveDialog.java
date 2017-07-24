package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panya on 30/5/2560.
 */

public class RemoveDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_remove_item, null);
    }
}
