package edu.rosehulman.lix4.petlf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.rosehulman.lix4.petlf.fragments.AccountFragment;
import edu.rosehulman.lix4.petlf.fragments.InfoDetailFragment;
import edu.rosehulman.lix4.petlf.fragments.LostInfoListFragment;
import edu.rosehulman.lix4.petlf.fragments.WelcomeFragment;
import edu.rosehulman.lix4.petlf.models.Post;

public class MainActivity extends AppCompatActivity implements AccountFragment.CallBack, LostInfoListFragment.Callback {
    private Fragment mCurrentFragment = null;
    private BottomNavigationView mNavigation;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private OnCompleteListener mOnCompleteListener;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser mUser;

    public MainActivity(Post post) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mCurrentFragment = new WelcomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, mCurrentFragment);
        ft.commit();

    }

    private void initializeListeners() {
//        mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d("=====>>>>>>", "Current user: " + user);
                if (user != null) {
                    mUser = user;
                }
            }
        };

        mOnCompleteListener = new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("========>>>>", "Log in failed");
                }
            }
        };
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mCurrentFragment = new WelcomeFragment();
                    break;
                case R.id.navigation_lost:
                    if (mUser != null) {
                        mCurrentFragment = LostInfoListFragment.newInstance("LOST", mUser.getUid());
                    } else {
                        mCurrentFragment = LostInfoListFragment.newInstance("LOST", "no user here");
                    }
                    break;
                case R.id.navigation_found:
                    if (mUser != null) {
                        mCurrentFragment = LostInfoListFragment.newInstance("FOUND", mUser.getUid());
                    } else {
                        mCurrentFragment = LostInfoListFragment.newInstance("FOUND","no user here");
                    }
                    break;
                case R.id.navigation_notifications:
                    mCurrentFragment = new AccountFragment();
                    break;
            }
            if (mCurrentFragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content, mCurrentFragment);
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
    public void onPostSelected(Post post, int position) {
        InfoDetailFragment mInfoDetailFragment = InfoDetailFragment.newInstance(post.getTitle(), post.getDescription(), post.getSize().toString(), post.getBreed());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, mInfoDetailFragment);
        ft.commit();
    }
}
