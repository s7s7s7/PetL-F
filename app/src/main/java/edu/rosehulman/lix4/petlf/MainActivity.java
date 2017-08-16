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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

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
        MyPostFragment.MPFCallback,
        InfoDetailFragment.IDFCallback {

    private static final int RECHOOSE_IMAGE_REQUEST = 2;
    private static final int CHOOSE_POTRAIT_PICTURE = 3;
    //Making this two fields is to control UI according to Login state.
    private WelcomeFragment mWelcomeFragment = new WelcomeFragment();
    private String mTag = null;
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
    private Uri newImageUri;
    private Button mUploadButton;
    private Uri mPotrait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.newImageUri = null;
        this.mPotrait = null;

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mTag = "welcome";
        ft.add(R.id.content, mWelcomeFragment, mTag);
        ft.commit();

        mAuth = FirebaseAuth.getInstance();

        initilizeListener();

        MyFirebaseMessagingService mService = new MyFirebaseMessagingService(this);
        mService.onCreate();
    }

    private void initilizeListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("======>>>>>>>>>>>>","Change current user!!!\n");
                    ConstantUser.setCurrentUser(user);
                    if (mPotrait != null){
                        uploadPotraitPic();
                    }
                } else {
                    ConstantUser.removeCurrentUser();
                }

                //Refresh the fragment after login state changed.
                Fragment frg = getSupportFragmentManager().findFragmentByTag(mTag);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
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
                    mTag = "welcome";
                    break;
                case R.id.navigation_lost:
                    mTag = "infoList";
                    if (ConstantUser.hasUser()) {
                        mInfoFragment = LostInfoListFragment.newInstance("LOST", ConstantUser.currentUser.getUid());
                        fragmentSelected = mInfoFragment;
                    } else {
                        mInfoFragment = LostInfoListFragment.newInstance("LOST", "no user here");
                        fragmentSelected = mInfoFragment;
                    }
                    break;
                case R.id.navigation_found:
                    mTag = "infoList";
                    if (ConstantUser.hasUser()) {
                        mInfoFragment = LostInfoListFragment.newInstance("FOUND", ConstantUser.currentUser.getUid());
                        fragmentSelected = mInfoFragment;
                    } else {
                        mInfoFragment = LostInfoListFragment.newInstance("FOUND", "no user here");
                        fragmentSelected = mInfoFragment;
                    }
                    break;
                case R.id.navigation_account:
                    mTag = "account";
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
                ft.replace(R.id.content, fragmentSelected, mTag);
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
        mUploadButton = (Button) view.findViewById(R.id.upload_image_button);

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePotrait();
            }
        });

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
                if (switsh) {
                    signin();
                } else {
                    //sign up and login user in automatically
                    String email = mEmailEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();
                    String confirmedPassword = mConfirmationPasswordEditText.getText().toString();
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

                    if (!password.equals(confirmedPassword)) {
                        showError(getString(R.string.invalid_confirmation_password));
                        cancelLogin = true;
                    }

                    if (!cancelLogin) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(mOnCompleteListener);
                        hideKeyboard();
                    }
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(mOnCompleteListener);

//                    try {
//                        uploadPotraitPic();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    //update user imageUrl and alias
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                            .build();

//                    user.updateProfile(profileUpdates)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//                                    }
//                                }
//                            });
                }
                mWelcomeFragment.controlButtons(true);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPotrait = null;
            }
        });
        builder.show();
    }

    private void uploadPotraitPic() {

//        int i = 0;
//        while(ConstantUser.currentUser==null){
//            i++;

//            if(i>=30){
//                Log.d("=======>>>>>>>","upload failed\n");
//                throw new InterruptedException();
//                return;
//            }
//        }
//        Log.d("======>>>>>>", "start upload\n");
        if (this.mPotrait != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference().child(ConstantUser.currentUser.getUid());
            StorageReference imgRef = reference.child("Potrait");

            UploadTask uploadTask = imgRef.putFile(this.mPotrait);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("===================>>>", "uploadImg failed");
                }
            });
            this.mPotrait = null;
        }
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
        Log.d("signin: ", email + password + cancelLogin + "");
        if (!cancelLogin) {
            Log.d("here", "inside");
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
        InfoDetailFragment mInfoDetailFragment = InfoDetailFragment.newInstance(post.getTitle(), post.getDescription(), post.getSize().toString(), post.getBreed(), post.getKey());
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

    public void reChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "ReChoose Picture"), RECHOOSE_IMAGE_REQUEST);
    }

    public void choosePotrait(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Potrait"), CHOOSE_POTRAIT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri file = data.getData();

            mInfoFragment.uploadImage(file);
        } else if (requestCode == RECHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            this.newImageUri = data.getData();
        } else if (requestCode == CHOOSE_POTRAIT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){
            this.mPotrait = data.getData();
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
        final Button reupload = (Button) view.findViewById(R.id.reUpload_image_button);

        Uri temp;
        reupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reChooseImage();
            }
        });


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
                if (newImageUri != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(post.getKey()).child("PetImg");

                    UploadTask uploadTask = storageReference.putFile(newImageUri);
                }

                newImageUri = null;
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

    @Override
    public void showImage(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This is the picture for the pet");
        View view = getLayoutInflater().inflate(R.layout.imagedetaildialog, null);
        builder.setView(view);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(key).child("PetImg");

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);

        builder.setPositiveButton(android.R.string.ok, null);

        builder.create().show();
    }
}
