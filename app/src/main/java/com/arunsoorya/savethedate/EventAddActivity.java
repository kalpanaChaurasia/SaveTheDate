package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.model.StoryVO;
import com.arunsoorya.savethedate.utils.DateChangeListener;
import com.arunsoorya.savethedate.utils.DatePickerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAddActivity extends BaseActivity implements View.OnClickListener, DateChangeListener {
    @BindView(R.id.event_name)
    TextInputEditText eventName;
    @BindView(R.id.event_desc)
    TextInputEditText eventDesc;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.choose_date)
    TextView chooseDate;
    private DatabaseReference mDatabase;
    private StoryVO storyVO;
    private static DateChangeListener dateChangeListener;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        selectedDate = Calendar.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        submit.setOnClickListener(this);
        chooseDate.setOnClickListener(this);
        dateChangeListener = this;
        if (getIntent() != null)
            storyVO = getIntent().getParcelableExtra("data");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                submit();
                break;
            case R.id.choose_date:
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);
                DialogFragment newFragment =  DatePickerFragment.getInstance(new int[]{year,month,day});
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    private void submit() {
        String seldDate = String.valueOf(selectedDate.getTimeInMillis());
        String selectedDateWithoutYear = generateEventId(selectedDate);

        String eventPath = getEventsPath() + selectedDateWithoutYear.concat("/");
        String keyEvent = mDatabase.child(eventPath).push().getKey();
        String keyEventStory = mDatabase.child(getStoryEventPath(storyVO.getStoryId())).push().getKey();

        EventVO eventVO = new EventVO(storyVO.getStoryId(), eventName.getText().toString(),
                eventDesc.getText().toString(), seldDate);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(eventPath.concat(keyEvent), eventVO.toMap());
        //updating event inside stories
        childUpdates.put(getStoryEventPath(storyVO.getStoryId()).concat("/") + keyEventStory, eventVO.toMap());
        mDatabase.updateChildren(childUpdates);
        mDatabase.addValueEventListener(valueEventListener);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        chooseDate.setText(day + "-" + month + "-" + year);
//        chooseDate.setTag(new int[]{year, month, day});
        selectedDate = getSelectedDate(year, month, day);
    }

}