package edu.rosehulman.lix4.petlf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import edu.rosehulman.lix4.petlf.fragments.AccountFragment;
import edu.rosehulman.lix4.petlf.fragments.LostInfoListFragment;
import edu.rosehulman.lix4.petlf.fragments.WelcomeFragment;

public class MainActivity extends AppCompatActivity implements AccountFragment.CallBack {
    private Fragment mCurrentFragment = null;
    private BottomNavigationView mNavigation;

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mCurrentFragment = new WelcomeFragment();
                    break;
                case R.id.navigation_lost:
                    mCurrentFragment = new LostInfoListFragment();
                    break;
                case R.id.navigation_found:
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
}
