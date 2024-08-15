package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position){
        Fragment fragment = null;
        if(position == 0)
            fragment = new NewBookFragment();
        else if(position == 1)
            fragment = new SpeechFragment();
        else if(position == 2)
            fragment = new ResearchFragment();
        else if(position == 3)
            fragment = new ThesisFragment();
        else
            fragment = new DissertationFragment();
        return fragment;
    }
    @Override
    public int getCount(){
        return 5;
    }
    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        if(position == 0)
            title = "Sách mới";
        else if(position == 1)
            title = "Nghiên cứu khoa học";
        else if(position == 2 )
            title = "Khóa luận";
        else if(position == 3)
            title = "Luận án";
        else
            title = "Luận văn";
        return title;
    }


}
