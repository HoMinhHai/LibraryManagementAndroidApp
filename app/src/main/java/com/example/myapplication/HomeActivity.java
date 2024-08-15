package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    FrameLayout frameFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        bottomNavigationView=(BottomNavigationView)
                findViewById(R.id.bottom_nav);
        frameFragment=(FrameLayout)findViewById(R.id.frameFragment);
        loadFragment(new MainFragment());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                if(item.getItemId() == R.id.navigation_home){
                    loadFragment(new MainFragment());
                    return true;
                }
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", null);
                    String password = sharedPreferences.getString("password", null);
                    if(username == null || password == null)
                        loadFragment(new LogoutFragment());
                    else
                        loadFragment(new AccountFragment());
                    return true;
                }

            }
        });
    }
    public void loadFragment(androidx.fragment.app.Fragment
                                     fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.frameFragment,fragment);
        ft.commit();
    }
}