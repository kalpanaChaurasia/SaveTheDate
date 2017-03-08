package com.arunsoorya.savethedate.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsoorya.savethedate.BaseActivity;
import com.arunsoorya.savethedate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public Context context;
    private ViewGroup progressLayout;
    private ViewGroup container;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = (ViewGroup) view.findViewById(R.id.container);
        progressLayout = (ViewGroup) view.findViewById(R.id.progress_layout);
    }

    public View getBaseView() {
        return container;
    }

    protected void setContentLayout(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(context).inflate(layoutId, null, false);
        container.addView(view);
        progressLayout.setVisibility(View.GONE);
        onViewCreate(view);
    }


    protected void onViewCreate(View view){

    }
    public BaseActivity getBaseInstance() {
        return (BaseActivity) context;
    }

    protected void showLoading() {
        container.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    protected void dismissLoading() {
        container.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
    }

}
