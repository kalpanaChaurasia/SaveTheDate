package com.arunsoorya.savethedate;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arunsoorya.savethedate.adapter.EventAdapter;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.DateChangeListener;
import com.arunsoorya.savethedate.utils.DatePickerFragment;
import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DateChangeListener {

    private String token;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.selectDate)
    TextView selectDate;

    private DataSnapshot dataSnapshot;
    private Calendar selectedDate;
    private List<EventVO> eventVOs = new ArrayList<>();
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showLoginUserData(navigationView.getHeaderView(0));
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        pushNewItemAddToTheEnd();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new EventAdapter(eventVOs, this));
        selectDate.setOnClickListener(this);
        selectedDate = Calendar.getInstance();
        showLoading();
        rootRef = FirebaseDatabase.getInstance().getReference(getEventsPath());
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
                DialogFragment newFragment = DatePickerFragment.getInstance(new int[]{year, month, day});
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    ValueEventListener eventListListener = new ValueEventListener() {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HomeActivity.this.dataSnapshot = dataSnapshot;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate.getTimeInMillis());
        calendar.set(Calendar.YEAR, 0);
        DataSnapshot eventSnapShot = dataSnapshot.child(String.valueOf(calendar.getTimeInMillis()));
        for (DataSnapshot postSnapshot : eventSnapShot.getChildren()) {
            eventVO = postSnapshot.getValue(EventVO.class);
            eventVOs.add(eventVO);
        }
        pushNewItemAddToTheEnd();
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    private void showLoginUserData(View navigationView) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String nameS = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();


            TextView desc = (TextView) navigationView.findViewById(R.id.detail);
            TextView name = (TextView) navigationView.findViewById(R.id.name);

            name.setText(nameS);
            desc.setText(email);
            token = user.getUid();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rootRef.removeEventListener(eventListListener);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_story) {
            navigate(StoryListActivity.class);
        } else if (id == R.id.nav_share) {
//            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//                    .setMessage(getString(R.string.invitation_message))
//                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
//                    .setCallToActionText(getString(R.string.invitation_cta))
//                    .build();
//            startActivityForResult(intent, REQUEST_INVITE);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDateSet(int year, int month, int day) {
        selectDate.setText(day + "-" + month + "-" + year);
//        chooseDate.setTag(new int[]{year, month, day});
        selectedDate = getSelectedDate(year, month, day);
        showEventsOnTheDate();
    }
}
