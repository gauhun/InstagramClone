package com.gsr.instagramgsr.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentStatePagerAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private final HashMap<String, Integer> mFragmentNumber = new HashMap<>();
    private final HashMap<Integer, String> mFragmentName = new HashMap<>();

    public FragmentStatePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String fragmentName){
        mFragmentList.add(fragment);
        mFragments.put(fragment, mFragmentList.size() - 1 );
        mFragmentNumber.put(fragmentName, mFragmentList.size() -1 );
        mFragmentName.put(mFragmentList.size() -1 , fragmentName);
    }


    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentNumber.containsKey(fragmentName)){
            return mFragmentNumber.get(fragmentName);
        }else {
            return null;
        }
    }


    public String getFragmentName(Integer fragmentNum){
        if(mFragmentName.containsKey(fragmentNum)){
            return mFragmentName.get(fragmentNum);
        }else {
            return null;
        }
    }



}
