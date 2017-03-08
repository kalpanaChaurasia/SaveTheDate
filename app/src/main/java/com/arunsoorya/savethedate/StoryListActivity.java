package com.arunsoorya.savethedate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.arunsoorya.savethedate.adapter.StoryAdapter;
import com.arunsoorya.savethedate.model.StoryVO;
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

public class StoryListActivity extends BaseActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.content_story_list);
        ButterKnife.bind(this);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                navigate(StoryActivity.class, "storyAdd");
//
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


}
