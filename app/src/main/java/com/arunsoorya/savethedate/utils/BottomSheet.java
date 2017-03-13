package com.arunsoorya.savethedate.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.model.EventVO;

/**
 * Created by arunsoorya on 12/03/17.
 */

public class BottomSheet extends BottomSheetDialogFragment {

    public static BottomSheet newInstance(EventVO eventVO) {
        BottomSheet dialog = new BottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", eventVO);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_modal, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//            ((BottomSheetBehavior) behavior).setPeekHeight(10);
        }
        TextView header = (TextView) dialog.findViewById(R.id.header);
        TextView desc = (TextView) dialog.findViewById(R.id.desc);

        EventVO eventVO = (EventVO) getArguments().get("data");
        if (eventVO != null) {
            header.setText(eventVO.getEventName());
            desc.setText(eventVO.getEventDesc());
        }
    }

}