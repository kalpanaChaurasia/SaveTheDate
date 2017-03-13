package com.arunsoorya.savethedate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunsoorya.savethedate.adapter.EventAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.model.StoryVO;
import com.arunsoorya.savethedate.utils.Constants;
import com.arunsoorya.savethedate.utils.MyDialog;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsInStories extends BaseActivity implements View.OnClickListener,
        RecyclerClickListener, RecyclerClickListener.eventEditListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.event_desc)
    TextView eventDesc;

    @BindView(R.id.event_date)
    TextView chooseDate;
    @BindView(R.id.goToStory)
    Button goToStory;

    @BindView(R.id.edit)
    ImageView edit;
    private StoryVO storyVO;

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
        initialize();
        setEventDataToView();
    }

    private void initialize() {
        goToStory.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EventAdapter(eventVOs, this, false, this));

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Query query = mDatabase.child(getStoryEventPath(event.getStoryId())).orderByChild("eventDate");
        if (!TextUtils.isEmpty(event.getStoryId())) {
            Query query = mDatabase.child(getStoryPath() + "/" + event.getStoryId());
            query.addValueEventListener(eventListListener);
        }
    }

    private void setEventDataToView() {
        getSupportActionBar().setTitle(event.getEventName());
        eventDesc.setText(event.getEventDesc());

        chooseDate.setText(Utils.getFormatedTime(event.getEventDate()));

        goToStory.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        if (TextUtils.isEmpty(event.getStoryId())) {
            goToStory.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            EventVO event = data.getParcelableExtra("eventVo");
            int param = data.getIntExtra("params", 0);
            if (event.getEventKey().equalsIgnoreCase(this.event.getEventKey())) {
                this.event = event;
                if (param == Constants.ADD_EVENT_DELETE)
                    finish();
                else
                    setEventDataToView();
            }

            if (param == Constants.ADD_EVENT_STORY_ADDED) {
                if (!TextUtils.isEmpty(event.getStoryId())) {
                    Query query = mDatabase.child(getStoryPath() + "/" + event.getStoryId());
                    query.addValueEventListener(eventListListener);
                }
            }
        }
    }

    ValueEventListener eventListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            DataSnapshot eventsSnapshot = dataSnapshot.child(EVENTIDS);
            storyVO = dataSnapshot.getValue(StoryVO.class);
            EventVO eventVO;
            eventVOs.clear();
            for (DataSnapshot postSnapshot : eventsSnapshot.getChildren()) {
                eventVO = postSnapshot.getValue(EventVO.class);
                eventVOs.add(eventVO);
            }
            Collections.sort(eventVOs, sortBasedOnDateComparator);
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
        showBottomSheet(eventVOs.get(position));
    }

    @Override
    public void onDefaultClick(View v) {

    }

    @Override
    public void onEventEdit(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("eventVo", eventVOs.get(position));
        bundle.putParcelable("storyVo", storyVO);
        navigateWithData(EventAddActivity.class, bundle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goToStory:
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", storyVO);
                navigateWithData(StoryActivity.class, bundle);
                break;
            case R.id.edit:
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("eventVo", event);
                navigateWithData(EventAddActivity.class, bundle1);
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
