package com.gsr.instagramgsr.utils;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gsr.instagramgsr.Likes.LikesActivity;
import com.gsr.instagramgsr.Home.MainActivity;
import com.gsr.instagramgsr.Profile.ProfileActivity;
import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.Search.SearchActivity;
import com.gsr.instagramgsr.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: setup bottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }


    public static void enableNavigation(final Context context, BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home:
                        context.startActivity(new Intent(context, MainActivity.class)); //Activity Number 0
                        break;
                    case R.id.share:
                        context.startActivity(new Intent(context, ShareActivity.class)); //Activity Number 1
                        break;
                    case R.id.add:
                        context.startActivity(new Intent(context, SearchActivity.class)); //Activity Number 2
                        break;
                    case R.id.following:
                        context.startActivity(new Intent(context, LikesActivity.class)); //Activity Number 3
                        break;
                    case R.id.profile:
                        context.startActivity(new Intent(context, ProfileActivity.class)); //Activity Number 4
                        break;
                }

                return false;
            }
        });
    }
}
