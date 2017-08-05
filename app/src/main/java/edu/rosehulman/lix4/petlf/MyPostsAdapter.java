package edu.rosehulman.lix4.petlf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.lix4.petlf.models.Post;

/**
 * Created by phillee on 7/2/2017.
 */

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {
    private Context mContext;
    private List<Post> mPosts;
    private DatabaseReference mPostsRef;
    private Query myPostsRef;

    public MyPostsAdapter(Context context) {
        mContext = context;
        mPosts = new ArrayList<>();

//        myPostsRef = mPostsRef.orderByChild("").equalTo("");
//        myPostsRef.addChildEventListener(new MyPostsChildEventListener());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myPostView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_card_view, parent, false);
        return new ViewHolder(myPostView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post current = mPosts.get(position);
        holder.titleTextView.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private Button deleteButton;

        public ViewHolder(View postView) {
            super(postView);
//            titleTextView = postView.findViewById();
        }
    }


    private class MyPostsChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }
}
