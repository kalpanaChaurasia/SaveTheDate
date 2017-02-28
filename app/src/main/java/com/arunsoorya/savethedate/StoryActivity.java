package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryActivity extends BaseActivity implements View.OnClickListener, RecyclerClickListener {
    @BindView(R.id.story_details)
    TextInputEditText storyDetails;
    @BindView(R.id.story_name)
    TextInputEditText storyName;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.submit)
    Button submit;
    //    @BindView(R.id.fab)
//    FloatingActionButton fab;
    private DatabaseReference mDatabase;
    private StoryVO storyVO;
    private List<EventVO> eventVOs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_story);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getIntent() != null && getIntent().hasExtra("storyAdd")) {
            recyclerView.setVisibility(View.GONE);
//            fab.setVisibility(View.GONE);
            submit.setOnClickListener(this);
        } else {

            if (getIntent() != null) {
                storyVO = getIntent().getParcelableExtra("data");

                storyDetails.setText(storyVO.getStoryDesc());
                storyName.setText(storyVO.getStoryName());

                storyDetails.setEnabled(false);
                storyName.setEnabled(false);
                showEventList();
            }

            submit.setVisibility(View.GONE);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("storyVo", storyVO);
//                    navigateWithData(EventAddActivity.class, bundle);
//
//                }
//            });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                submit();
                break;
        }
    }

    private void showEventList() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new EventAdapter(eventVOs, this));

        mDatabase = FirebaseDatabase.getInstance().getReference(getStoryEventPath(storyVO.getStoryId()));
        mDatabase.addValueEventListener(eventListListener);
    }


    private void submit() {
        String todayKey = getTimeString();
        String key = mDatabase.child(getStoryPath()).push().getKey();
        StoryVO storyVO = new StoryVO(key, storyName.getText().toString(), storyDetails.getText().toString(), todayKey);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(getStoryPath() + key, storyVO.toMap());
        mDatabase.updateChildren(childUpdates);
        mDatabase.addListenerForSingleValueEvent(eventAddListener);
    }

    private void pushNewItemAddToTheEnd() {
        EventVO eventVO = new EventVO();
        eventVO.setViewType(Utils.RECYCLE_TYPE_ADD);
        eventVOs.add(eventVO);
    }

    ValueEventListener eventAddListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//dataSnapshot.
            showToast("story added");
            finish();
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
        mDatabase.removeEventListener(eventListListener);
        mDatabase.removeEventListener(eventAddListener);
    }

    @Override
    public void onItemClick(int position, View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("storyVo", storyVO);
        bundle.putParcelable("eventVo", eventVOs.get(position));
        navigateWithData(EventAddActivity.class, bundle);
    }

    @Override
    public void onDefaultClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("storyVo", storyVO);
        navigateWithData(EventAddActivity.class, bundle);
    }
}
