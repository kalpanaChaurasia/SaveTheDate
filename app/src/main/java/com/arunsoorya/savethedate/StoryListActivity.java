package com.arunsoorya.savethedate;

import android.content.Intent;
import android.os.Bundle;
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

public class StoryListActivity extends BaseActivity implements RecyclerClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<StoryVO> storyVOs = new ArrayList();
    private DatabaseReference mDatabase;

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

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new StoryAdapter(storyVOs, this));

        mDatabase = FirebaseDatabase.getInstance().getReference(getStoryPath());
        mDatabase.addValueEventListener(valueEventListener);
        showLoading();
    }

    private void pushNewItemAddToTheEnd() {
        StoryVO storyVO = new StoryVO();
        storyVO.setViewType(Utils.RECYCLE_TYPE_ADD);
        storyVOs.add(storyVO);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dismissLoading();
            storyVOs.clear();
            StoryVO storyVO;
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                storyVO = postSnapshot.getValue(StoryVO.class);
                storyVOs.add(storyVO);
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
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    public void onItemClick(int position, View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", storyVOs.get(position));
        navigateWithData(StoryActivity.class, bundle);
    }

    @Override
    public void onDefaultClick(View v) {
        navigate(StoryActivity.class, "storyAdd");
    }
}
