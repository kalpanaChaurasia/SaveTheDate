package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arunsoorya.savethedate.adapter.EventAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.MyDialog;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsInStories extends BaseActivity implements RecyclerClickListener, RecyclerClickListener.eventEditListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.event_name)
    TextView eventName;
    @BindView(R.id.event_desc)
    TextView eventDesc;

    @BindView(R.id.event_date)
    TextView chooseDate;


    private DatabaseReference mDatabase;
    private List<EventVO> eventVOs = new ArrayList<>();
    private EventVO event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_events_in_stories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("eventVo")) {
            event = getIntent().getParcelableExtra("eventVo");
        }
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EventAdapter(eventVOs, this));

        mDatabase = FirebaseDatabase.getInstance().getReference(getStoryEventPath(event.getStoryId()));
        mDatabase.addValueEventListener(eventListListener);
        setEventDataToView();
    }

    private void setEventDataToView() {
        eventName.setText(event.getEventName());
        eventDesc.setText(event.getEventDesc());
        chooseDate.setText(event.getEventDate());

        chooseDate.setText(Utils.getFormatedTime(event.getEventDate()));
    }


    ValueEventListener eventListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            EventVO eventVO;
            eventVOs.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                eventVO = postSnapshot.getValue(EventVO.class);
                eventVOs.add(eventVO);
            }
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(eventListListener);
    }

    @Override
    public void onItemClick(int position, View v) {
        MyDialog dialog = (MyDialog) MyDialog.getInstance(eventVOs.get(position));
        dialog.show(getSupportFragmentManager(), "dia");
    }

    @Override
    public void onDefaultClick(View v) {

    }

    @Override
    public void onEventEdit(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("eventVo", eventVOs.get(position));
        navigateWithData(EventAddActivity.class, bundle);
    }
}
