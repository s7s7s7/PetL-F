package edu.rosehulman.lix4.petlf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MyPostActivity extends AppCompatActivity {
    private MyPostsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        mAdapter = new MyPostsAdapter(MyPostActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_my_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

    }
}
