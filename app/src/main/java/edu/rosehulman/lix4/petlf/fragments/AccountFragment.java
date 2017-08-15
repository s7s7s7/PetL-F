package edu.rosehulman.lix4.petlf.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.rosehulman.lix4.petlf.ConstantUser;
import edu.rosehulman.lix4.petlf.MyPostActivity;
import edu.rosehulman.lix4.petlf.R;
import edu.rosehulman.lix4.petlf.models.User;

public class AccountFragment extends Fragment {

    private static final String ARG_USER = "currentUser";
    private AFCallBack mAFCallBack;

    private User mUser;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView emailTextView = (TextView) view.findViewById(R.id.email_display_text_view);
        ImageView profile_pic_image_view = (ImageView) view.findViewById(R.id.profile_pic_image_view);
        Button myPostsButton = (Button) view.findViewById(R.id.button_myposts);
        Button logoutButton = (Button) view.findViewById(R.id.button_logout);
        Button resetPasswordButton = (Button) view.findViewById(R.id.button_reset_password);
        if (ConstantUser.hasUser()) {
            emailTextView.setText(String.format(getResources().getString(R.string.email_diaplay_text), ConstantUser.currentUser.getEmail()));

            //load pic of user
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(ConstantUser.currentUser.getUid());
            Glide.with(getActivity())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(profile_pic_image_view);

            myPostsButton.setVisibility(View.VISIBLE);
            resetPasswordButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            resetPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Send reset password email to your email?");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String emailAddress = auth.getCurrentUser().getEmail();
                            auth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Successfully Reset Your password!");
                                                builder.setMessage("System will sign you out. Please sign in using your new password.");
                                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mAFCallBack.setNavigationId(R.id.navigation_home);
                                                        mAFCallBack.signOut();
                                                    }
                                                });
                                                builder.create().show();
                                            }
                                        }
                                    });
                        }
                    });
                    builder.create().show();
                }
            });
            myPostsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAFCallBack.switchToMyPosts();
                }
            });
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAFCallBack.setNavigationId(R.id.navigation_home);
                    mAFCallBack.signOut();
                }
            });
        } else {
            emailTextView.setText(R.string.email_not_log_in);
            myPostsButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
            resetPasswordButton.setVisibility(View.INVISIBLE);
            profile_pic_image_view.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

        Button contactUsButton = (Button) view.findViewById(R.id.button_contact_us);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUs();
            }
        });
        return view;
    }

    private void contactUs() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"lix4@rose-hulman.edu"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "From me");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello Pet L&F Development Team,");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void startMyPostActivity() {
        Intent myPostActivity = new Intent(getContext(), MyPostActivity.class);
        startActivity(myPostActivity);
    }

    public interface AFCallBack {
        void setNavigationId(int id);

//        void deletePost(Post post);
//
//        void editPost(Post post);

        void switchToMyPosts();

        void signOut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AFCallBack) {
            mAFCallBack = (AFCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AFCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAFCallBack = null;
    }

}
