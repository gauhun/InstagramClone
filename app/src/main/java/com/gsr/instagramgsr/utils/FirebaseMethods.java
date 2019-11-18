package com.gsr.instagramgsr.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gsr.instagramgsr.Home.MainActivity;
import com.gsr.instagramgsr.Models.User;
import com.gsr.instagramgsr.Models.UserAccountSetting;
import com.gsr.instagramgsr.Models.UserSettings;
import com.gsr.instagramgsr.R;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private  Context mContext;
    private FirebaseAuth mAuth;
    private String userId;
    private boolean signIn, register;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    public FirebaseMethods(Context mContext) {
        mAuth = FirebaseAuth.getInstance();
        this.mContext = mContext;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if(userId != null){
            userId = mAuth.getCurrentUser().getUid();
        }
    }



   /* public boolean checkIfUserNameExist(String username, DataSnapshot dataSnapshot){
        Log.d(TAG, "checkIfUserNameExist: checking if " + username + " already exist");

        User user = new User();

        for(DataSnapshot ds : dataSnapshot.child(userId).getChildren()){
            //this will get the User class data from database
            user.setUsername(ds.getValue(User.class).getUsername());


            if(StringManiplution.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUserNameExist: FOUND A MATCH " + user.getUsername());
                return true;
            }

        }

        return false;
    }*/


    //------------------Register New Email-------------------------
    public void registerNewEmail(String email, String password, String username){

        mAuth.createUserWithEmailAndPassword(email, password)
                //here we do not pass context, because we are in separate class
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //sending email verification
                            emailVerification();
                            userId = mAuth.getCurrentUser().getUid();
                        }else {
                            Log.d(TAG, "onComplete: unsuccesful Register");
                            Toast.makeText(mContext, "Unsuccessful Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //------------------Email Verification-------------------------
    private void emailVerification(){

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: successfully send email address!");

                            }else {
                                Toast.makeText(mContext, "couldn't send verification email! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    //------------------Login User-------------------------
    private boolean loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Sign in success
                            Log.d(TAG, "onComplete: signInWithEmail: Successful signIn");
                            signIn = true;
                        }else {
                            //If sign in fail display the message to user
                            Log.d(TAG, "onComplete: ");
                            signIn = false;
                        }
                    }
                });

        return signIn;
    }


    //------------------Add New user to Database------------------------
    public void addNewUser(String email, String username, String description, String website, String profile_photo ){

        //This will add user data to firebase database
        User user = new User(email, "0000000000", userId , StringManiplution.condenseUsername(username));
        databaseReference.child(mContext.getString(R.string.dbname_users))
                .child(userId)
                .setValue(user);

        //This will add user_account_setting to firebase database
        UserAccountSetting user_account_setting = new UserAccountSetting(description,
                StringManiplution.condenseUsername(username),
                0,
                0,
                0,
                "",
                StringManiplution.condenseUsername(username),
                "dummyweb123.com");

        databaseReference.child(mContext.getString(R.string.dbname_account_setting))
                .child(userId)
                .setValue(user_account_setting);
    }

    //----------------------Updating Username on database ------------------------------
    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: updaing username to " + username);

        databaseReference.child(mContext.getString(R.string.dbname_users))
                .child(userId)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        databaseReference.child(mContext.getString(R.string.dbname_account_setting))
                .child(userId)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }


    //----------------------Updating email on database ------------------------------
    public void updateEmail(String email){
        Log.d(TAG, "updateEmail: updaing email to " + email);

        databaseReference.child(mContext.getString(R.string.dbname_users))
                .child(userId)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }


    public void saveChanges(String display_name, String website, String description, String phoneNumber){

        if(display_name != null){
            databaseReference.child(mContext.getString(R.string.dbname_account_setting))
                    .child(userId)
                    .child(mContext.getString(R.string.field_displayName))
                    .setValue(display_name);
        }

        if(website != null){
            databaseReference.child(mContext.getString(R.string.dbname_account_setting))
                    .child(userId)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }

        if(description != null){
            databaseReference.child(mContext.getString(R.string.dbname_account_setting))
                    .child(userId)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }

        if(phoneNumber != null ){
            databaseReference.child(mContext.getString(R.string.dbname_users))
                    .child(userId)
                    .child(mContext.getString(R.string.field_phoneNumber))
                    .setValue(phoneNumber);
        }
    }

    //---------------------get Usersettings --------------------
    public UserSettings getUserSetting(DataSnapshot dataSnapshot){

        UserAccountSetting userAccountSetting = new UserAccountSetting();
        User user = new User();

        userId = mAuth.getCurrentUser().getUid();


        for(DataSnapshot ds : dataSnapshot.getChildren()) {

            //This try ensure there is no crash if there is null value in a field of User Account Setting
            try {
                    //This will ensure we are in correct database node
                    if (ds.getKey().equals(mContext.getString(R.string.dbname_account_setting))) {
                        Log.d(TAG, "getUserSetting: Datasnapshot : " + ds);

                        Log.d(TAG, "getUserSetting: Username : " + ds.child(userId).child("display_name").getValue(String.class));

                        //Saving data to settings
                        userAccountSetting.setDisplay_name(
                                ds.child(userId)
                                .getValue(UserAccountSetting.class)
                                .getDisplay_name()
                        );

                        userAccountSetting.setUsername(
                                ds.child(userId)
                                        .getValue(UserAccountSetting.class)
                                        .getUsername()
                        );

                        userAccountSetting.setPosts(
                                ds.child(userId)
                                        .getValue(UserAccountSetting.class)
                                        .getPosts()
                        );

                        userAccountSetting.setFollowers(
                                ds.child(userId)
                                        .getValue(UserAccountSetting.class)
                                        .getFollowers()
                        );

                        userAccountSetting.setFollowing(
                                ds.child(userId)
                                        .getValue(UserAccountSetting.class)
                                        .getFollowing()
                        );

                        userAccountSetting.setDescription(
                                ds.child(userId)
                                        .getValue(UserAccountSetting.class)
                                        .getDescription()
                        );

                        userAccountSetting.setWebsite(
                                ds.child(userId)
                                        .getValue(UserAccountSetting.class)
                                        .getWebsite()
                        );

                        userAccountSetting.setProfile_photo(
                                ds.child(userId)
                                .getValue(UserAccountSetting.class)
                                .getProfile_photo()
                        );

                        Log.d(TAG, "getUserAccountSetting: retrieved user_account_setting information : " + userAccountSetting);
                    }
                }catch(NullPointerException e){
                    Log.d(TAG, "getUserAccountSetting: NullPointerException " + e.getMessage());
                }


                //This try ensure there is no crash if there is null value in a field of User
                try {
                    if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                        Log.d(TAG, "getUserSetting: Inside User.");

                        //saving data to user
                        user.setUsername(ds.child(userId).getValue(User.class).getUsername());

                        user.setEmail(ds.child(userId).getValue(User.class).getEmail());

                        user.setPhone_number(ds.child(userId).getValue(User.class).getPhone_number());

                        user.setUser_id(ds.child(userId).getValue(User.class).getUser_id());

                        Log.d(TAG, "getUserAccountSetting: retrieved user information : " + user);
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, "getUserAccountSetting: NullPointerException " + e.getMessage());
                }
            }


        //Saving UserAccountSetting and User to userSetting
        UserSettings userSettings = new UserSettings();
        userSettings.setUser(user);
        userSettings.setUserAccountSetting(userAccountSetting);

        return userSettings;
    }




}
