package edu.rosehulman.lix4.petlf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import edu.rosehulman.lix4.petlf.fragments.AccountFragment;
import edu.rosehulman.lix4.petlf.fragments.InfoDetailFragment;
import edu.rosehulman.lix4.petlf.fragments.LostInfoListFragment;
import edu.rosehulman.lix4.petlf.fragments.MyPostFragment;
import edu.rosehulman.lix4.petlf.fragments.WelcomeFragment;
import edu.rosehulman.lix4.petlf.models.Post;

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
    private LostInfoListFragment mInfoFragment;
    final private static int PICK_IMAGE_REQUEST = 1;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmationPasswordEditText;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, mWelcomeFragment);
        ft.commit();

        mAuth = FirebaseAuth.getInstance();

        initilizeListener();
    }


    private void initilizeListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
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
                    showError(task.getException().getMessage());
                }
            }
        };
    }

    private void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.error);
        builder.setMessage(error);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
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
                        mInfoFragment = LostInfoListFragment.newInstance("LOST", ConstantUser.currentUser.getUid());
                        fragmentSelected = mInfoFragment;
                    } else {
                        mInfoFragment = LostInfoListFragment.newInstance("LOST", "no user here");
                        fragmentSelected = mInfoFragment;
                    }
                    break;
                case R.id.navigation_found:
                    if (ConstantUser.hasUser()) {
                        mInfoFragment = LostInfoListFragment.newInstance("FOUND", ConstantUser.currentUser.getUid());
                        fragmentSelected = mInfoFragment;
                    } else {
                        mInfoFragment = LostInfoListFragment.newInstance("FOUND", "no user here");
                        fragmentSelected = mInfoFragment;
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
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void showSignInUpDialog(final boolean switsh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_signup, null);
        mEmailEditText = (EditText) view.findViewById(R.id.edit_username_text_signup);
        mPasswordEditText = (EditText) view.findViewById(R.id.edit_password_text_signup);
        mConfirmationPasswordEditText = (EditText) view.findViewById(R.id.edit_password_confirm_text_signup);
        mEmailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_NEXT) {
                    mEmailEditText.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mEmailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    signin();
                    return true;
                }
                return false;
            }
        });
        TextView confirmationPasswordTitle = (TextView) view.findViewById(R.id.dialog_confirm_email_title_signup);
        if (switsh) {
            builder.setTitle(R.string.signin_dialog_title);
            mConfirmationPasswordEditText.setVisibility(View.INVISIBLE);
            confirmationPasswordTitle.setVisibility(View.INVISIBLE);
        } else {
            builder.setTitle(R.string.signup_dialog_title);
        }
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (switsh) {
                    //sign in
//                    mAuth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(mOnCompleteListener);
                    signin();
                } else {
                    //sign up and login user in automatically
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);
                    //update user imageUrl and alias
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
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

    public void signin() {
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancelLogin = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            showError(getString(R.string.invalid_password));
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(email)) {
            showError(getString(R.string.email_required));
            cancelLogin = true;
        } else if (!isEmailValid(email)) {
            showError(getString(R.string.invalid_email));
            cancelLogin = true;
        }

        if (!cancelLogin) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mOnCompleteListener);
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
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
    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri file = data.getData();

            mInfoFragment.uploadImage(file);
        }
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

        builder.setNegativeButton(android.R.string.cancel, null);
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
