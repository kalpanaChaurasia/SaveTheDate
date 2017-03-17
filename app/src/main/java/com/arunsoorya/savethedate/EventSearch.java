package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.arunsoorya.savethedate.adapter.EventAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventSearch extends BaseActivity implements RecyclerClickListener, RecyclerClickListener.eventEditListener, RecyclerClickListener.goToStoryListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private List<EventVO> eventVOs = new ArrayList<>();
private EventAdapter eventAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_event_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventVOs, this, false, this);
        recyclerView.setAdapter(eventAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Query query = mDatabase.child(getStoryEventPath(event.getStoryId())).orderByChild("eventDate");
        Query query = mDatabase.child(getEventsPath()).orderByKey();
        query.addValueEventListener(eventListListener);
        showLoading();
    }

    ValueEventListener eventListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dismissLoading();
            EventVO eventVO;
            eventVOs.clear();
            Iterable<DataSnapshot> map = dataSnapshot.getChildren();
            for (DataSnapshot snapshot : map) {
                Iterable<DataSnapshot> inner = snapshot.getChildren();
                for (DataSnapshot snapshot1 : inner) {
                    eventVO = snapshot1.getValue(EventVO.class);
                    eventVOs.add(eventVO);
                }
            }
            eventAdapter.updateList(eventVOs);
//            Collections.sort(eventVOs, sortBasedOnDateComparator);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
// Associate searchable configuration with the SearchView
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener);
        return true;
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            eventAdapter.filter(newText);
            return false;
        }
    };

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

    @Override
    public void onEventEdit(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("eventVo", eventVOs.get(position));
        navigateWithData(EventAddActivity.class, bundle);
    }

    @Override
    public void goToStory(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("storyId", eventVOs.get(position).getStoryId());
        navigateWithData(StoryActivity.class, bundle);
    }
}
