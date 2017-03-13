package com.arunsoorya.savethedate.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arunsoorya.savethedate.AddStory;
import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.StoryActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class StoriesFragment extends BaseFragment implements RecyclerClickListener, View.OnClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<StoryVO> storyVOs = new ArrayList();
    private DatabaseReference mDatabase;
    @BindView(R.id.new_event_layout)
    View newEventLayout;

    public StoriesFragment() {
        // Required empty public constructor
    }

    public static StoriesFragment newInstance() {
        StoriesFragment fragment = new StoriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentLayout(R.layout.fragment_stories);
    }

    @Override
    protected void onViewCreate(View view) {
//        super.onViewCreate(view);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        newEventLayout.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(new StoryAdapter(storyVOs, context, this));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(getBaseInstance().getStoryPath());
        mDatabase.keepSynced(true);
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
            if (storyVOs.size() != 0) {
                pushNewItemAddToTheEnd();
                recyclerView.getAdapter().notifyDataSetChanged();
                newEventLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
                newEventLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    public void onItemClick(int position, View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", storyVOs.get(position));
        getBaseInstance().navigateWithData(StoryActivity.class, bundle);
    }

    @Override
    public void onDefaultClick(View v) {
        getBaseInstance().navigate(AddStory.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_event_layout:
                getBaseInstance().navigate(AddStory.class);
                break;
        }
    }
}
