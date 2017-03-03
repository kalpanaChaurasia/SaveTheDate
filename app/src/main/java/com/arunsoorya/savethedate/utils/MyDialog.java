package com.arunsoorya.savethedate.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.model.EventVO;

/**
 * Created by arunsoorya on 03/03/17.
 */

public class MyDialog extends DialogFragment {

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.dialog, container, false);
//        getDialog().setTitle("Simple Dialog");
//        return rootView;
//    }
    public static DialogFragment getInstance(EventVO eventVO) {
        MyDialog dialog = new MyDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", eventVO);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        EventVO eventVO = (EventVO) getArguments().get("data");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(eventVO.getEventName());
        builder.setMessage(eventVO.getEventDesc());

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }
}
