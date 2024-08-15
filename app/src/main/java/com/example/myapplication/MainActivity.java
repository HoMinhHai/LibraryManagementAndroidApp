package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    FloatingSearchView searchView;
    ImageButton btnLogin;
    TabLayout tabLayout;
    ArrayList<String> data=new ArrayList<>();
    Spinner spFilter;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.view_pagerMain);
        searchView = findViewById(R.id.searchBar);
        viewPagerAdapter = new
                ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        spFilter = findViewById(R.id.sp_filter);
        this.initData();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,data);
        spFilter.setAdapter(adapter);
        addEvent();
    }
    private void initData()
    {
        data.add("Tìm theo tiêu đề");
        data.add("Chủ đề");
        data.add("Tác giả");
        data.add("Năm xuất bản");
        data.add("Nhà xuất bản");
    }
    private void addEvent(){

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("inputString", currentQuery);
                intent.putExtra("Category", spFilter.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }
}