package com.arunsoorya.savethedate.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arunsoorya.savethedate.EventAddActivity;
import com.arunsoorya.savethedate.EventsInStories;
import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.adapter.EventAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.DateChangeListener;
import com.arunsoorya.savethedate.utils.DatePickerFragment;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, DateChangeListener, RecyclerClickListener,
        RecyclerClickListener.eventEditListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.selectDate)
    Button selectDate;
    @BindView(R.id.new_event_layout)
    View newEventLayout;

    private DataSnapshot dataSnapshot;
    private Calendar selectedDate;
    private List<EventVO> eventVOs = new ArrayList<>();
    private DatabaseReference rootRef;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentLayout(R.layout.fragment_home);
    }

    @Override
    protected void onViewCreate(View view) {
//        super.onViewCreate(view);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(new EventAdapter(eventVOs, context, true, this));
        selectDate.setOnClickListener(this);
        newEventLayout.setOnClickListener(this);
        selectedDate = Calendar.getInstance();
        selectDate.setText(Utils.getFormattedDate(selectedDate));
        showLoading();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference(getBaseInstance().getEventsPath());
        rootRef.keepSynced(true);
        rootRef.addValueEventListener(eventListListener);
    }


    private void pushNewItemAddToTheEnd() {
        EventVO eventVO = new EventVO();
        eventVO.setViewType(Utils.RECYCLE_TYPE_ADD);
        eventVOs.add(eventVO);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectDate:
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);
                DatePickerFragment newFragment = DatePickerFragment.getInstance(new int[]{year, month, day});
                newFragment.setDateChangeListener(this);
                newFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case R.id.new_event_layout:
                getBaseInstance().navigate(EventAddActivity.class);
                break;
        }
    }

    ValueEventListener eventListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnap) {
            dataSnapshot = dataSnap;
            showEventsOnTheDate();
            dismissLoading();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void showEventsOnTheDate() {
        eventVOs.clear();
        EventVO eventVO;
        Calendar calendar = getBaseInstance().getSelectedDate(selectedDate);
        calendar.set(Calendar.YEAR, 0);
        if (dataSnapshot == null)
            return;
        DataSnapshot eventSnapShot = dataSnapshot.child(String.valueOf(calendar.getTimeInMillis()));
        for (DataSnapshot postSnapshot : eventSnapShot.getChildren()) {
            eventVO = postSnapshot.getValue(EventVO.class);
            eventVOs.add(eventVO);
        }
        if (eventVOs.size() != 0) {
            pushNewItemAddToTheEnd();
            recyclerView.getAdapter().notifyDataSetChanged();
            newEventLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            newEventLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootRef.removeEventListener(eventListListener);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        selectedDate = getBaseInstance().getSelectedDate(year, month, day);
        selectDate.setText(Utils.getFormattedDate(selectedDate));
        showEventsOnTheDate();
    }

    @Override
    public void onItemClick(int position, View v) {
        getBaseInstance().showBottomSheet(eventVOs.get(position));

    }

    @Override
    public void onDefaultClick(View v) {
        getBaseInstance().navigate(EventAddActivity.class);
    }

    @Override
    public void onEventEdit(View v, int position) {
        EventVO eventVO = eventVOs.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("eventVo", eventVOs.get(position));
        if (eventVO.getStoryId() == null)
            getBaseInstance().navigateWithData(EventAddActivity.class, bundle);
        else
            getBaseInstance().navigateWithData(EventsInStories.class, bundle);
    }
}
