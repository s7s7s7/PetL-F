package edu.rosehulman.lix4.petlf;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.rosehulman.lix4.petlf.models.Post;

/**
 * Created by sunq1 on 8/4/2017.
 */

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.ViewHolder> {

    private String mType;
    private DatabaseReference mInfoRef;
    private ArrayList<Post> mPosts;

    public InfoListAdapter(String type) {
        mType = type;
        if (mType.equals("LOST")) {
            mInfoRef = FirebaseDatabase.getInstance().getReference().child("lost");
        } else {
            mInfoRef = FirebaseDatabase.getInstance().getReference().child("found");
        }


    }

    @Override
    public InfoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_info_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoListAdapter.ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        final int temp = position;
        holder.mTitle.setText(post.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to detail fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void addPost(Post post) {
        mInfoRef.push().setValue(post);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.info_cardview_title);
        }
    }

    private class PostChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Post post = dataSnapshot.getValue(Post.class);
            post.setKey(dataSnapshot.getKey());
            mPosts.add(0, post);
            Log.d("------------->>>", "Post's title is: " + mPosts.get(0).getTitle());
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Post updatePost = dataSnapshot.getValue(Post.class);
            for(Post post:mPosts){
                if(post.getKey().equals(key)){
                    post.setValues(updatePost);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Post post : mPosts) {
                if (post.getKey().equals(key)) {
                    mPosts.remove(post);
                    notifyDataSetChanged();
                    return;
                }
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
