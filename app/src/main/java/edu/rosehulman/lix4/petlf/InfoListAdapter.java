package edu.rosehulman.lix4.petlf;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import edu.rosehulman.lix4.petlf.fragments.LostInfoListFragment;
import edu.rosehulman.lix4.petlf.models.Post;

/**
 * Created by sunq1 on 8/4/2017.
 */

public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.ViewHolder> {

    private Query myInfoRef;
    private String mType;
    private DatabaseReference mInfoRef;
    private ArrayList<Post> mPosts;
    private LostInfoListFragment.LILCallback mLILCallback;
    private Uri toUpload;

    public InfoListAdapter(String type, LostInfoListFragment.LILCallback callback) {
        mPosts = new ArrayList<>();
        mType = type;
//        if (mType.equals("LOST")) {
//            mInfoRef = FirebaseDatabase.getInstance().getReference().child("lost");
//        } else {
//            mInfoRef = FirebaseDatabase.getInstance().getReference().child("found");
//        }

        mInfoRef = FirebaseDatabase.getInstance().getReference();
        if (mType.equals("LOST")) {
            myInfoRef = mInfoRef.orderByChild("type").equalTo(false);
        } else {
            myInfoRef = mInfoRef.orderByChild("type").equalTo(true);
        }

        mLILCallback = callback;

        myInfoRef.addChildEventListener(new PostChildEventListener());


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
                mLILCallback.onPostSelected(post, temp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void addPost(Post post, Uri toUpload) {
        mInfoRef.push().setValue(post);
        this.toUpload = toUpload;
    }

    public void uploadImage(String key) {

        if (this.toUpload != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference().child(key);
            StorageReference imgRef = reference.child("PetImg");

            UploadTask uploadTask = imgRef.putFile(this.toUpload);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("===================>>>", "uploadImg failed");
                }
            });
        }

        this.toUpload = null;
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
            uploadImage(dataSnapshot.getKey());
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
