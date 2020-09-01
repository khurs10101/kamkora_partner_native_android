package com.khurshid.kamkorapartner.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khurshid.kamkorapartner.R;
import com.khurshid.kamkorapartner.view.fragments.HomeFragment;
import com.khurshid.kamkorapartner.view.fragments.OrderFragment;
import com.khurshid.kamkorapartner.view.fragments.ProfileFragment;
import com.khurshid.kamkorapartner.view.fragments.RewardsFragment;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.USEROBJECT, currentUser);
        switch (item.getItemId()) {
            case R.id.menu_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.menu_booking:
                selectedFragment = new OrderFragment();

                break;
            case R.id.menu_rewards:
                selectedFragment = new RewardsFragment();
                break;

            case R.id.menu_profile:
                selectedFragment = new ProfileFragment();
//                selectedFragment.setArguments(bundle);
                break;

        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();

        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();

//        //initially open the home fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    private void initViews() {

        //        sharedPreferences = getSharedPreferences(Constants.USERCREDS, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        isLogin = sharedPreferences.getString("username", null);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }
}