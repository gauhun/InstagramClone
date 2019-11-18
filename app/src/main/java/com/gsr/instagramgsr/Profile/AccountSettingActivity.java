package com.gsr.instagramgsr.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.utils.BottomNavigationViewHelper;
import com.gsr.instagramgsr.utils.FragmentStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class AccountSettingActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingActivity";
    private final Context mContext = AccountSettingActivity.this;

    ImageView backArrow;
    ArrayList<String> settings;
    ListView listView;
    ViewPager viewPager;
    FragmentStatePagerAdapter fragmentStatePagerAdapter;
    RelativeLayout relativeLayout1;
    ArrayAdapter adapter;

    private final static int ACTIVITY_NUM = 4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_setting);

        backArrow = findViewById(R.id.img_back_arrow);
        listView = findViewById(R.id.lv_account_setting_listView);
        viewPager = findViewById(R.id.container);
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager());
        relativeLayout1 = findViewById(R.id.relativeLayoutAccountSetting);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        settings = new ArrayList<>();
        settings.add(getString(R.string.edit_profile_fragment)); //list item 0
        settings.add(getString(R.string.sign_out_fragment)); //list item 1


        adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, settings);
        listView.setAdapter(adapter);

        setupFragments();

        setupBottomNavigation();

        getIncomingIntent();

    }

    private void getIncomingIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "getIncomingIntent: recieving intent from activity : " + getString(R.string.profile_activity));
            setupViewPager(fragmentStatePagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }
    }

    private void setupViewPager(int fragmentNumber){
        relativeLayout1.setVisibility(View.GONE);
        viewPager.setAdapter(fragmentStatePagerAdapter);
        viewPager.setCurrentItem(fragmentNumber);
    }


    private void setupFragments(){
        fragmentStatePagerAdapter.addFragment(new FragmentEditProfile(), "Edit Profile"); //index 0
        fragmentStatePagerAdapter.addFragment(new FragmentSignOutProfile(), "SignOut Profile");//index 1

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //this will open a tapped list item fragment in a accountSettingActivity
                setupViewPager(position);
            }
        });
    }

    private void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

}
