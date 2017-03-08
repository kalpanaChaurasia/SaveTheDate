package com.arunsoorya.savethedate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.model.StoryVO;
import com.arunsoorya.savethedate.utils.Constants;
import com.arunsoorya.savethedate.utils.DateChangeListener;
import com.arunsoorya.savethedate.utils.DatePickerFragment;
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

public class EventAddActivity extends BaseActivity implements View.OnClickListener, DateChangeListener {
    @BindView(R.id.event_name)
    TextInputEditText eventName;
    @BindView(R.id.event_desc)
    TextInputEditText eventDesc;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.choose_date)
    TextView chooseDate;
    //    @BindView(R.id.edit)
//    ImageView edit;
    @BindView(R.id.selectedStory)
    TextView selectedStory;

    private DatabaseReference getStoryRef;
    private DatabaseReference updateRef;
    private String storyId;
    private Calendar selectedDate;
    private Calendar peviousDate;
    private EventVO event;
    private List<StoryVO> storyVOs = new ArrayList();
    private List<String> storyVOsName = new ArrayList();
    private boolean isEdit;
    private boolean isDateChanged;
    private boolean isStoryAdded;
    private boolean isEventDelete;
    private String passedEventId;
    private StoryVO storyVO;
    private EventVO eventNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_event_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        selectedDate = Calendar.getInstance();
        peviousDate = Calendar.getInstance();
        updateRef = FirebaseDatabase.getInstance().getReference();
        submit.setOnClickListener(this);
        chooseDate.setOnClickListener(this);
        selectedStory.setOnClickListener(this);

        if (getIntent() != null) {
            if (getIntent().hasExtra("storyVo")) {
                storyVO = getIntent().getParcelableExtra("storyVo");
                storyId = storyVO.getStoryId();
                selectedStory.setText(storyVO.getStoryName());
            } else {
                selectedStory.setOnClickListener(this);
            }
            if (getIntent().hasExtra("eventVo")) {
                event = getIntent().getParcelableExtra("eventVo");
                passedEventId = event.getEventId();
                isEdit = true;
                storyId = event.getStoryId();
                setEventDataToView();
            }
        }

    }

    private void setEventDataToView() {
        eventName.setText(event.getEventName());
        eventDesc.setText(event.getEventDesc());
        chooseDate.setText(event.getEventDate());

        selectedDate.setTimeInMillis(Long.parseLong(event.getEventDate()));
        peviousDate.setTimeInMillis(Long.parseLong(event.getEventDate()));

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
                DatePickerFragment newFragment = DatePickerFragment.getInstance(new int[]{year, month, day});
                newFragment.show(getSupportFragmentManager(), "datePicker");
                newFragment.setDateChangeListener(this);
                break;
            case R.id.selectedStory:
                showStoryList();
                break;
        }
    }

    private void showStoryList() {

        getStoryRef = FirebaseDatabase.getInstance().getReference(getStoryPath());
        getStoryRef.addListenerForSingleValueEvent(storyListListener);
//        showLoading();
    }

    private boolean isValid() {
        return (!isEmpty(eventName.getText().toString())
                && !isEmpty(eventDesc.getText().toString())
                && !isEmpty(chooseDate.getText().toString()));
    }

    private void submit() {
        if (isEdit && isDateChanged) {
            // for updating..first we are deleting and then adding
            removeStoryAndEventRef();
        } else {
            submitEventAndStories();
        }
    }

    private void removeStoryAndEventRef() {

        String oldDateWithoutYear = generateEventId(peviousDate);
        //updating the event in story and events child
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(getStoryEventPath(storyVO.getStoryId()).concat("/") + event.getEventKey(), null);
        childUpdates.put(getEventsPath().concat(oldDateWithoutYear).concat("/") + event.getEventKey(), null);
        updateRef.updateChildren(childUpdates);
        updateRef.addListenerForSingleValueEvent(removeEventListener);

    }

    private void submitEventAndStories() {

        String selectedDateWithoutYear = generateEventId(selectedDate);

        String eventPath = getEventsPath() + selectedDateWithoutYear.concat("/");
        String keyEvent;
        if (!isDateChanged && isEdit) {
            keyEvent = event.getEventKey();
        } else {
            keyEvent = updateRef.child(eventPath).push().getKey();
        }


        String seldDate = String.valueOf(selectedDate.getTimeInMillis());
        //creating new event and setting event key
        eventNew = new EventVO(storyId, eventName.getText().toString(),
                eventDesc.getText().toString(), seldDate);
        eventNew.setEventKey(keyEvent);

        //updating the event in story and events child
        Map<String, Object> childUpdates = new HashMap<>();
        if (storyId != null) {

            childUpdates.put(getStoryEventPath(storyId).concat("/") + keyEvent, eventNew.toMap());
        }
        childUpdates.put(eventPath.concat(keyEvent), eventNew.toMap());

        updateRef.updateChildren(childUpdates);
        updateRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public String generateEventId(Calendar timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp.getTimeInMillis());
        calendar.set(Calendar.YEAR, 0);
        return String.valueOf(calendar.getTimeInMillis());
    }

    private void deleteWhenDateofStoryChanges() {


    }

    ValueEventListener removeEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!isEventDelete) {
                submitEventAndStories();
            } else {
                setCallbackData(Constants.ADD_EVENT_DELETE);
                finish();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            showToast("Event added");
            if (isStoryAdded)
                setCallbackData(Constants.ADD_EVENT_STORY_ADDED);
            else
                setCallbackData(0);
            finish();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    void setCallbackData(int param) {
        Intent bundle = new Intent();
        bundle.putExtra("eventVo", eventNew == null ? event : eventNew);
        bundle.putExtra("params", param);
        setResult(RESULT_OK, bundle);
    }

    ValueEventListener storyListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            storyVOs.clear();
            storyVOsName.clear();
            StoryVO storyVO;
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                storyVO = postSnapshot.getValue(StoryVO.class);
                storyVOs.add(storyVO);
                storyVOsName.add(storyVO.getStoryName());
            }
            pushNewItemAddToTheEnd();
            showStoryListDialog();
//            dismissLoading();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void showStoryListDialog() {
        CharSequence[] array = storyVOsName.toArray(new CharSequence[storyVOsName.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Choose a Day")
                .setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        storyId = storyVOs.get(i).getStoryId();
                        isStoryAdded = true;
                        selectedStory.setText(storyVOs.get(i).getStoryName());
                    }
                });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }

    private void pushNewItemAddToTheEnd() {
        StoryVO storyVO = new StoryVO();
        storyVO.setViewType(Utils.RECYCLE_TYPE_ADD);
        storyVOs.add(storyVO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateRef.removeEventListener(valueEventListener);
        updateRef.removeEventListener(removeEventListener);
        if (getStoryRef != null)
            getStoryRef.removeEventListener(storyListListener);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        selectedDate = getSelectedDate(year, month, day);
        setdateInText(selectedDate);
        isDateChanged = true;

    }

    private void setdateInText(Calendar calendar) {
        chooseDate.setText(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onDeleteConfirm() {
        isEventDelete = true;
        removeStoryAndEventRef();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            showDeleteAlert("Do you want to Delete?");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}