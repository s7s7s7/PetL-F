package edu.rosehulman.lix4.petlf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MyPostActivity extends AppCompatActivity {
    private MyPostsAdapter mAdapter;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        Intent intent = getIntent();
//        mUserId = intent.getStringExtra("uid");
        mUserId = ConstantUser.currentUser.getUserId();
        Log.d("mUserId: ", mUserId + "");
        mAdapter = new MyPostsAdapter(MyPostActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_my_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

    }
}
