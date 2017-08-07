package edu.rosehulman.lix4.petlf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.rosehulman.lix4.petlf.fragments.AccountFragment;
import edu.rosehulman.lix4.petlf.fragments.InfoDetailFragment;
import edu.rosehulman.lix4.petlf.fragments.LostInfoListFragment;
import edu.rosehulman.lix4.petlf.fragments.MyPostFragment;
import edu.rosehulman.lix4.petlf.fragments.WelcomeFragment;
import edu.rosehulman.lix4.petlf.models.Post;
import edu.rosehulman.lix4.petlf.models.User;

public class MainActivity extends AppCompatActivity implements
        AccountFragment.AFCallBack,
        WelcomeFragment.WFCallBack,
        LostInfoListFragment.LILCallback,
        MyPostFragment.MPFCallback {
    //Making this two fields is to control UI according to Login state.
    private WelcomeFragment mWelcomeFragment = new WelcomeFragment();
    private BottomNavigationView mNavigation;
    //    ConstantUser.currentUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener mOnCompleteListener;
    private MyPostFragment myPostFragment;

//    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, mWelcomeFragment);
        ft.commit();

        ConstantUser.setCurrentAuth(FirebaseAuth.getInstance());

//        ConstantUser.setCurrentUser(FirebaseAuth.getInstance());
//        ConstantUser.currentUser = FirebaseAuth.getInstance();
        initilizeListener();
    }


    private void initilizeListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    User newUser = new User();
//                    newUser.setUserId(user.getUid());
//                    newUser.setEmail(user.getEmail());
//                    newUser.setImageUrl(user.getPhotoUrl());
                    ConstantUser.setCurrentUser(user);
                } else {
                    ConstantUser.removeCurrentUser();
                }
            }
        };
        mOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    Log.d("onComplete failed: ", task.getException().toString());
                }
            }
        };
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragmentSelected = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentSelected = mWelcomeFragment;
                    break;
                case R.id.navigation_lost:
                    if (ConstantUser.hasUser()) {
                        fragmentSelected = LostInfoListFragment.newInstance("LOST", ConstantUser.currentUser.getUid());
                    } else {
                        fragmentSelected = LostInfoListFragment.newInstance("LOST", "no user here");
                    }
                    break;
                case R.id.navigation_found:
                    if (ConstantUser.hasUser()) {
                        fragmentSelected = LostInfoListFragment.newInstance("FOUND", ConstantUser.currentUser.getUid());
                    } else {
                        fragmentSelected = LostInfoListFragment.newInstance("FOUND", "no user here");
                    }
                    break;
                case R.id.navigation_account:
                    fragmentSelected = new AccountFragment();
                    break;
            }
            if (fragmentSelected != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Slide slideTransition = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    slideTransition = new Slide(Gravity.RIGHT);
                    slideTransition.setDuration(200);
                }
                fragmentSelected.setEnterTransition(slideTransition);
                ft.replace(R.id.content, fragmentSelected);
                ft.commit();
            }
            return true;
        }

    };


    @Override
    public void setNavigationId(int id) {
        mNavigation.setSelectedItemId(id);
    }


    @Override
    public void signOut() {
        ConstantUser.currentAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        ConstantUser.currentAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            ConstantUser.currentAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void showSignInUpDialog(final boolean switsh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_signup, null);
        final EditText emailEditText = (EditText) view.findViewById(R.id.edit_username_text_signup);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.edit_password_text_signup);
        final EditText confirmationPasswordEditText = (EditText) view.findViewById(R.id.edit_password_confirm_text_signup);
        TextView confirmationPasswordTitle = (TextView) view.findViewById(R.id.dialog_confirm_email_title_signup);
        if (switsh) {
            builder.setTitle(R.string.signin_dialog_title);
            confirmationPasswordEditText.setVisibility(View.INVISIBLE);
            confirmationPasswordTitle.setVisibility(View.INVISIBLE);
        } else {
            builder.setTitle(R.string.signup_dialog_title);
        }
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (switsh) {
                    //sign in
                    ConstantUser.currentAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);
                } else {
                    //sign up and login user in automatically
                    ConstantUser.currentAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);
                    ConstantUser.currentAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);
                    //update user imageUrl and alias
                    FirebaseUser user = ConstantUser.currentAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                }
                mWelcomeFragment.controlButtons(true);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    @Override
    public void onPostSelected(Post post, int position) {
        InfoDetailFragment mInfoDetailFragment = InfoDetailFragment.newInstance(post.getTitle(), post.getDescription(), post.getSize().toString(), post.getBreed());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, mInfoDetailFragment);
        ft.addToBackStack("detail");
        ft.commit();
    }


    @Override
    public void editMyPost(final Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit your post");
        View view = getLayoutInflater().inflate(R.layout.edit_post_dialog, null);
        builder.setView(view);

        final EditText titleEditView = (EditText) view.findViewById(R.id.post_change_title_edit);
        final EditText breedEditView = (EditText) view.findViewById(R.id.post_change_breed_edit);
        final EditText sizeEditView = (EditText) view.findViewById(R.id.post_change_description_edit);
        final EditText descriptionEditView = (EditText) view.findViewById(R.id.post_change_size_edit);


        titleEditView.setHint(post.getTitle());
        breedEditView.setHint(post.getBreed());
        descriptionEditView.setHint(post.getDescription());
        sizeEditView.setHint(post.getSize());



        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = post.getDescription();
                String title = post.getTitle();
                String size = post.getSize();
                String breed = post.getBreed();

                if (!titleEditView.getText().toString().equals("")) {
                    title = titleEditView.getText().toString();
                }
                if (!breedEditView.getText().toString().equals("")) {
                    breed = titleEditView.getText().toString();
                }
                if (!sizeEditView.getText().toString().equals("")) {
                    size = titleEditView.getText().toString();
                }
                if (!descriptionEditView.getText().toString().equals("")) {
                    description = descriptionEditView.getText().toString();
                }

                myPostFragment.update(post, title, breed, size, description);
            }
        });

        builder.setNegativeButton(android.R.string.cancel,null);
        builder.create().show();
    }

    @Override
    public void deleteMyPost(Post post) {
//        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference();
//        tempRef.child(post.getKey()).removeValue();
        myPostFragment.remove(post);
    }

    @Override
    public void switchToMyPosts() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        myPostFragment = new MyPostFragment();
        ft.replace(R.id.content, myPostFragment);
        ft.addToBackStack("myPosts");
        ft.commit();
    }
}
