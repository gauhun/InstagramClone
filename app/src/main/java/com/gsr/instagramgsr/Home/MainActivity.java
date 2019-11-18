package com.gsr.instagramgsr.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gsr.instagramgsr.Login.LoginActivity;
import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.utils.BottomNavigationViewHelper;
import com.gsr.instagramgsr.utils.SectionPagerAdapter;
import com.gsr.instagramgsr.utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity{

    private Context mContext = MainActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageLoader();

        setupBottomNavigation();

        setupViewPager();

        setupFirebase();


    }
    
    


    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment()); //index 0
        adapter.addFragment(new HomeFragment());   //index 1
        adapter.addFragment(new MessageFragment());//index 2
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        //setting icons for fragment in tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_linked_camera_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.insta_logo);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_send_black_24dp);

    }


    /*-------------- BottomNavigationViewEx SETUP -----------------*/
    private void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }



    /* --------------------------- Firebase Stuff -----------------------------*/
    private void setupFirebase(){
        Log.d(TAG, "setupFirebase: setting up firebase ");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if user is signed in (non - null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Check if User is logedIn or not
        checkCurrentUser(currentUser);

        if(currentUser != null){
            Log.d(TAG, "onStart onAuthStateChanged: singed in with ID : " + currentUser.getUid() );
        }else {
            Log.d(TAG, "onStart onAuthStateChanged: signed out ");
        }
    }

    //This part will deal with login if your are already loged in past
    private void checkCurrentUser(FirebaseUser user){
        if(user == null){

            finish();
            //Jump to login activity
            startActivity(new Intent(mContext , LoginActivity.class));
        }
    }

}
