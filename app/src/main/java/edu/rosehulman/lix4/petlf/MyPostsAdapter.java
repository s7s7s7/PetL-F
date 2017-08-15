package edu.rosehulman.lix4.petlf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.lix4.petlf.fragments.MyPostFragment;
import edu.rosehulman.lix4.petlf.models.Post;

/**
 * Created by phillee on 7/2/2017.
 */

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {
    //    private Context mContext;
    private List<Post> mPosts;
    private DatabaseReference mPostsRef;
    private Query myPostsRef;
    private MyPostFragment.MPFCallback mCallback;


    public MyPostsAdapter(MyPostFragment.MPFCallback callback) {
//        mContext = context;
        mCallback = callback;
        mPosts = new ArrayList<>();
        mPostsRef = FirebaseDatabase.getInstance().getReference();
        myPostsRef = mPostsRef.orderByChild("uid").equalTo(ConstantUser.currentUser.getUid());
        myPostsRef.addChildEventListener(new MyPostsChildEventListener());
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
        final Post current = mPosts.get(position);
        holder.titleTextView.setText(current.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCallback.editMyPost(current);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View v) {
                mCallback.deleteMyPost(current);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d("=========>>>: ", mPosts.size()+"");
        return mPosts.size();
    }

    public void remove(Post post) {
        mPostsRef.child(post.getKey()).removeValue();
    }

    public void update(Post post, String title, String breed, String size, String description) {
        post.setTitle(title);
        post.setBreed(breed);
        post.setDescription(description);
        post.setSize(size);

        mPostsRef.child(post.getKey()).setValue(post);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
//        private Button deleteButton;

        public ViewHolder(View postView) {
            super(postView);
            titleTextView = (TextView) postView.findViewById(R.id.my_post_card_title);
//            titleTextView = postView.findViewById();
        }
    }


    private class MyPostsChildEventListener implements ChildEventListener {
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
            for (Post post : mPosts) {
                if (post.getKey().equals(key)) {
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
