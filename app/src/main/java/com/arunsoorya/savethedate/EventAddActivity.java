package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arunsoorya.savethedate.adapter.StoryNameAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.model.StoryVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAddActivity extends BaseActivity implements View.OnClickListener, DateChangeListener, RecyclerClickListener {
    @BindView(R.id.event_name)
    TextInputEditText eventName;
    @BindView(R.id.event_desc)
    TextInputEditText eventDesc;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.choose_date)
    TextView chooseDate;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private DatabaseReference getStoryRef;
    private DatabaseReference updateRef;
    private String storyId;
    private static DateChangeListener dateChangeListener;
    private Calendar selectedDate;
    private EventVO event;
    private List<StoryVO> storyVOs = new ArrayList();
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_event_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        selectedDate = Calendar.getInstance();
        updateRef = FirebaseDatabase.getInstance().getReference();
        submit.setOnClickListener(this);
        chooseDate.setOnClickListener(this);
        dateChangeListener = this;
        if (getIntent() != null) {
            if (getIntent().hasExtra("storyVo")) {
                StoryVO storyVO = getIntent().getParcelableExtra("storyVo");
                storyId = storyVO.getStoryId();
            }
            if (getIntent().hasExtra("eventVo")) {
                event = getIntent().getParcelableExtra("eventVo");
                isEdit = true;
                storyId = event.getStoryId();
                setEventDataToView();
            }
        }
        showStoryList();
    }

    private void setEventDataToView() {
        eventName.setText(event.getEventName());
        eventDesc.setText(event.getEventDesc());
        chooseDate.setText(event.getEventDate());

        selectedDate.setTimeInMillis(Long.parseLong(event.getEventDate()));
        setdateInText(selectedDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (isValid())
                    submit();
                else
                    showToast("plz fill all fields");
                break;
            case R.id.choose_date:
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);
                DialogFragment newFragment = DatePickerFragment.getInstance(new int[]{year, month, day});
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    private void showStoryList() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new StoryNameAdapter(storyVOs, this, storyId));

        getStoryRef = FirebaseDatabase.getInstance().getReference(getStoryPath());
        getStoryRef.addValueEventListener(storyListListener);
        showLoading();
    }

    private boolean isValid() {
        return (!isEmpty(eventName.getText().toString())
                && !isEmpty(eventDesc.getText().toString())
                && !isEmpty(chooseDate.getText().toString()));
    }

    private void submit() {
        String seldDate = String.valueOf(selectedDate.getTimeInMillis());
        String selectedDateWithoutYear = generateEventId(selectedDate);

        String eventPath = getEventsPath() + selectedDateWithoutYear.concat("/");
        String keyEvent;
        if (isEdit)
            keyEvent = event.getEventKey();
        else
            keyEvent = updateRef.child(eventPath).push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        EventVO eventVO = new EventVO(storyId, eventName.getText().toString(),
                eventDesc.getText().toString(), seldDate);
        eventVO.setEventKey(keyEvent);
        if (storyId != null) {
//            String keyEventStory = updateRef.child(getStoryEventPath(storyId)).push().getKey();
            //updating event inside stories
            childUpdates.put(getStoryEventPath(storyId).concat("/") + keyEvent, eventVO.toMap());
        }

        childUpdates.put(eventPath.concat(keyEvent), eventVO.toMap());

        updateRef.updateChildren(childUpdates);
        updateRef.addValueEventListener(valueEventListener);
    }

    public String generateEventId(Calendar timeStamp) {
        Calendar calendar = timeStamp;
        calendar.set(Calendar.YEAR, 0);
        return String.valueOf(calendar.getTimeInMillis());
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            showToast("Event added");
            setResult(RESULT_OK);
            finish();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener storyListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            storyVOs.clear();
            StoryVO storyVO;
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                storyVO = postSnapshot.getValue(StoryVO.class);
                storyVOs.add(storyVO);
            }
            pushNewItemAddToTheEnd();
            recyclerView.getAdapter().notifyDataSetChanged();
            dismissLoading();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void pushNewItemAddToTheEnd() {
        StoryVO storyVO = new StoryVO();
        storyVO.setViewType(Utils.RECYCLE_TYPE_ADD);
        storyVOs.add(storyVO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateRef.removeEventListener(valueEventListener);
        getStoryRef.removeEventListener(storyListListener);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        selectedDate = getSelectedDate(year, month, day);
        setdateInText(selectedDate);

    }

    private void setdateInText(Calendar calendar) {
        chooseDate.setText(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onItemClick(int position, View v) {
        storyId = storyVOs.get(position).getStoryId();
    }

    @Override
    public void onDefaultClick(View v) {
        navigate(StoryActivity.class, "storyAdd");
    }
}