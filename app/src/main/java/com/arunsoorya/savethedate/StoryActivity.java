package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunsoorya.savethedate.adapter.EventAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.model.StoryVO;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryActivity extends BaseActivity implements View.OnClickListener, RecyclerClickListener, RecyclerClickListener.eventEditListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.event_desc)
    TextView storyDetails;
    @BindView(R.id.edit)
    ImageView edit;

    //    @BindView(R.id.fab)
//    FloatingActionButton fab;
    private DatabaseReference getAllStoryRef, getSelectedStoryRef, deleteRef;
    private StoryVO storyVO;
    private List<EventVO> eventVOs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_story);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        deleteRef = FirebaseDatabase.getInstance().getReference();
        getAllStoryRef = FirebaseDatabase.getInstance().getReference();

        String storyId = "";
        if (getIntent() != null) {
            if (getIntent().hasExtra("data")) {
                storyVO = getIntent().getParcelableExtra("data");
                storyId = storyVO.getStoryId();
                setDateToView(storyVO);
            }
            if (getIntent().hasExtra("storyId")) {
                storyId = getIntent().getStringExtra("storyId");
            }
            edit.setOnClickListener(this);
            getStoryDetails(storyId);
            showEventList(storyId);
        }


    }

    private void setDateToView(StoryVO storyVO) {
        getSupportActionBar().setTitle(storyVO.getStoryName());
        storyDetails.setText(storyVO.getStoryDesc());
    }

    private void getStoryDetails(String storyId) {

        getSelectedStoryRef = FirebaseDatabase.getInstance().getReference(getStoryPath() + "/" + storyId);
        getSelectedStoryRef.addValueEventListener(storyDetailsListener);
    }


    private void showEventList(String storyId) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EventAdapter(eventVOs, this, false, this));

        getAllStoryRef = FirebaseDatabase.getInstance().getReference(getStoryEventPath(storyId));
        getAllStoryRef.addValueEventListener(eventListListener);
    }


    private void pushNewItemAddToTheEnd() {
        EventVO eventVO = new EventVO();
        eventVO.setViewType(Utils.RECYCLE_TYPE_ADD);
        eventVOs.add(eventVO);
    }

    ValueEventListener storyDetailsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            storyVO = dataSnapshot.getValue(StoryVO.class);
            if (storyVO != null)
                setDateToView(storyVO);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ValueEventListener eventListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            EventVO eventVO;
            eventVOs.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                eventVO = postSnapshot.getValue(EventVO.class);
                eventVOs.add(eventVO);
            }
            Collections.sort(eventVOs, sortBasedOnDateComparator);
            pushNewItemAddToTheEnd();
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAllStoryRef.removeEventListener(eventListListener);
        deleteRef.removeEventListener(removeEventListener);
        getSelectedStoryRef.removeEventListener(storyDetailsListener);
    }

    @Override
    public void onItemClick(int position, View v) {
        showBottomSheet(eventVOs.get(position));
    }

    @Override
    public void onDefaultClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("storyVo", storyVO);
        navigateWithData(EventAddActivity.class, bundle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                Bundle bundle = new Bundle();
                bundle.putParcelable("storyVo", storyVO);
                navigateWithData(AddStory.class, bundle);
                break;
        }
    }

    @Override
    public void onEventEdit(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("eventVo", eventVOs.get(position));
        bundle.putParcelable("storyVo", storyVO);
        navigateWithData(EventAddActivity.class, bundle);
    }

    ValueEventListener removeEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//dataSnapshot.
            showToast("story removed");
            finish();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onDeleteConfirm() {
        removeStoryAndEventRef();

    }

    private void removeStoryAndEventRef() {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(getStoryPath().concat(storyVO.getStoryId()), null);
        for (EventVO eventVO : eventVOs) {
            if (eventVO.getEventKey() == null)
                continue;
            String storyIdPath = getEventsPath().concat(eventVO.getEventDateWithoutYear()).concat("/")
                    .concat(eventVO.getEventKey()).concat("/").concat("storyId");
            childUpdates.put(storyIdPath, null);
        }
        deleteRef.updateChildren(childUpdates);
        deleteRef.addListenerForSingleValueEvent(removeEventListener);
        getSelectedStoryRef.removeEventListener(storyDetailsListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                finish();
                return true;
            case R.id.action_delete:
                showDeleteAlert("Do you want to Delete?");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
