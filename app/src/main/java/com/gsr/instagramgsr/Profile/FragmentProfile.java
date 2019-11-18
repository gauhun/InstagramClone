package com.gsr.instagramgsr.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gsr.instagramgsr.Login.LoginActivity;
import com.gsr.instagramgsr.Models.UserAccountSetting;
import com.gsr.instagramgsr.Models.UserSettings;
import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.utils.BottomNavigationViewHelper;
import com.gsr.instagramgsr.utils.FirebaseMethods;
import com.gsr.instagramgsr.utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {

    private static final String TAG = "FragmentProfile";

    private TextView mFollowers, mFollowing, mPost, mDisplayName, mUserName, mWebsite, mDiscription;
    private ProgressBar progressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;
    private static final int ACTIVITY_NUM = 4;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;
    private FirebaseMethods firebaseMethods;
    TextView editProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mFollowers = view.findViewById(R.id.tv_followers);
        mFollowing = view.findViewById(R.id.tv_following);
        mPost = view.findViewById(R.id.tv_posts);
        mUserName = view.findViewById(R.id.tv_profile_user_name);
        mDiscription = view.findViewById(R.id.tv_profile_user_description);
        mWebsite = view.findViewById(R.id.tv_profile_user_website);
        mDisplayName = view.findViewById(R.id.tv_display_name);
        progressBar = view.findViewById(R.id.progressBar);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        gridView = view.findViewById(R.id.profile_gridView);
        toolbar = view.findViewById(R.id.profile_toolbar);
        profileMenu = view.findViewById(R.id.img_menu);
        bottomNavigationView = view.findViewById(R.id.bottomNavigationViewBar);
        mContext = getActivity();
        firebaseMethods = new FirebaseMethods(mContext);

        editProfile = view.findViewById(R.id.tv_edit_profile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
            }
        });


        setupBottomNavigation();
        setupToolbar();

        setupFirebase();

        Log.d(TAG, "onCreateView: Started.");
        return view;
    }

    private void setProfileWidget(UserSettings userSettings){
        Log.d(TAG, "setProfileWidget: setting widget with data getting from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidget: profile Photo : " + mProfilePhoto);
        UserAccountSetting userAccountSetting = userSettings.getUserAccountSetting();

        UniversalImageLoader.setImage(userAccountSetting.getProfile_photo(), mProfilePhoto, null, "");
        //set the userAccount settings to textViews
        mUserName.setText(userAccountSetting.getUsername());
        mDisplayName.setText(userAccountSetting.getDisplay_name());
        mPost.setText(String.valueOf(userAccountSetting.getPosts()));
        mFollowers.setText(String.valueOf(userAccountSetting.getFollowers()));
        mFollowing.setText(String.valueOf(userAccountSetting.getFollowing()));
        mDiscription.setText(userAccountSetting.getDescription());
        mWebsite.setText(userAccountSetting.getWebsite());

    }


    //-----------setup Toolbar--------------
    private void setupToolbar(){
        //right now i don't know why we used it :)
        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to account setting.");
                startActivity(new Intent(mContext, AccountSettingActivity.class));
            }
        });
    }

    /**
     * ----------- Setting Up BottomNavigationView-----------
     */
    private void setupBottomNavigation(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    //----------------Firebase Stuff---------------
    private void setupFirebase(){



        Log.d(TAG, "setupFirebase: setting up firebase ");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        userId = mAuth.getUid();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Sign In.");
                }else {
                    Log.d(TAG, "onAuthStateChanged: Sign Out.");
                }
            }
        };

        //This will get data from database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // Log.d(TAG, "onDataChange: dataSnapshot : " + dataSnapshot);
                    //****------------RETRIEVE USER INFORMATION FROM DATBASE ----------------********//
                    //You have data from database
                    setProfileWidget(firebaseMethods.getUserSetting(dataSnapshot));

                    //****-------------RETRIEVE IMAGE FOR THE USER IN QUESTION ---------****//
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //Check if user is signed in (non - null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Log.d(TAG, "onStart onAuthStateChanged: singed in with ID : " + currentUser.getUid() );
        }else {
            Log.d(TAG, "onStart onAuthStateChanged: signed out ");
        }
    }

}
