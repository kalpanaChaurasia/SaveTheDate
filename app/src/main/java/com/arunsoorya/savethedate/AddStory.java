package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.arunsoorya.savethedate.model.StoryVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStory extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.story_details)
    TextInputEditText storyDetails;
    @BindView(R.id.story_name)
    TextInputEditText storyName;
    @BindView(R.id.submit)
    Button submit;
    private DatabaseReference mDatabase;
    private boolean isStoryEdit;
    private StoryVO storyVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_add_story);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (getIntent() != null && getIntent().hasExtra("storyVo")) {
            storyVO = getIntent().getParcelableExtra("storyVo");
            storyDetails.setText(storyVO.getStoryDesc());
            storyName.setText(storyVO.getStoryName());
            isStoryEdit = true;
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

    private void submit() {
        showHeaderLoding(storyName, storyDetails, submit);
        String todayKey = getTimeString();
        String key;
        if (!isStoryEdit) {
            key = mDatabase.child(getStoryPath()).push().getKey();
        } else {
            key = storyVO.getStoryId();
        }
        StoryVO storyVO = new StoryVO(key, storyName.getText().toString(), storyDetails.getText().toString(), todayKey);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(getStoryPath() + key, storyVO.toMap());
        mDatabase.updateChildren(childUpdates);
        mDatabase.addListenerForSingleValueEvent(eventAddListener);
//        showLoading();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(eventAddListener);
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
