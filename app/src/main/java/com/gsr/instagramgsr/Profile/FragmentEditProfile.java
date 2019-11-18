package com.gsr.instagramgsr.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gsr.instagramgsr.Dialog.ConfirmPasswordDialog;
import com.gsr.instagramgsr.Models.User;
import com.gsr.instagramgsr.Models.UserAccountSetting;
import com.gsr.instagramgsr.Models.UserSettings;
import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.utils.BottomNavigationViewHelper;
import com.gsr.instagramgsr.utils.FirebaseMethods;
import com.gsr.instagramgsr.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentEditProfile extends Fragment implements ConfirmPasswordDialog.onConfirmPasswordListener{


    @Override
    public void confirmPassword(String password) {
        Log.d(TAG, "confirmPassword: recieve the password : " + password);

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        // Prompt the user to re-provide their sign-in credentials


        ////////////////Reauthentication of email
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User re-authenticated.");

                            ////////////////////Check if email is not already in use
                            mAuth.fetchSignInMethodsForEmail(semail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if(task.getResult().getSignInMethods() == null){
                                        Log.d(TAG, "onComplete: gmail is already in use!");
                                        Toast.makeText(getActivity(), "Email is already in use.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        //Email is not in use

                                        ////////////////////Updating email address
                                        mAuth.getCurrentUser().updateEmail(semail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "User email address updated.");
                                                            Toast.makeText(getActivity(), "email updated.", Toast.LENGTH_SHORT).show();

                                                            /////////////////////Update the email address to database user
                                                            firebaseMethods.updateEmail(semail);

                                                        }else {
                                                            Log.d(TAG, "onComplete: User email isn't updated");
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }else{
                            Log.d(TAG, "User is not re-authenticated.");
                            Toast.makeText(getActivity(), "User is not re-authenticated.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }

    private static final String TAG = "FragmentEditProfile";
    ImageView backarrow, editProfileImage;
    UniversalImageLoader universalImageLoader;

    String semail;

    //---Firebase stuff---
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;
    private FirebaseMethods firebaseMethods;
    private UserSettings userSettings;

    private CircleImageView profile_photo;
    private TextView changePhoto;
    private EditText  profileName, username, website, description, email, phoneNumber;
    private ImageView saveChanges;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Log.d(TAG, "onCreateView: setup fragment edit profile");

        editProfileImage = view.findViewById(R.id.profile_photo);

        backarrow = view.findViewById(R.id.img_back_arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });




        profile_photo = view.findViewById(R.id.profile_photo);
        changePhoto = view.findViewById(R.id.tv_change_photo);
        profileName = view.findViewById(R.id.et_profile_name);
        username = view.findViewById(R.id.et_username);
        website = view.findViewById(R.id.et_website);
        description = view.findViewById(R.id.et_discription);
        email = view.findViewById(R.id.et_gmail);
        phoneNumber = view.findViewById(R.id.et_phone_number);
        toolbar = view.findViewById(R.id.edit_toolbar);

        firebaseMethods = new FirebaseMethods(getActivity());

        //setProfileImage();

        setupFirebase();

        saveChanges = view.findViewById(R.id.img_check);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pressing save change.");
                saveProfileChanges();
            }
        });


        return view;
    }

    private void setProfileWidget(UserSettings settings){
        Log.d(TAG, "setProfileWidget: setting profile fields from data of firebase " + settings);

        UserAccountSetting accountSetting = settings.getUserAccountSetting();
        User user = settings.getUser();

        UniversalImageLoader.setImage(accountSetting.getProfile_photo(), profile_photo, null, "");
        profileName.setText(accountSetting.getDisplay_name());
        username.setText(user.getUsername());
        website.setText(accountSetting.getWebsite());
        description.setText(accountSetting.getDescription());
        email.setText(user.getEmail());
        phoneNumber.setText(String.valueOf(user.getPhone_number()));
    }

    private void saveProfileChanges(){
        final String sdisplayName = profileName.getText().toString();
        final String susername = username.getText().toString();
        final String swebsite = website.getText().toString();
        final String sdescription = description.getText().toString();
        semail = email.getText().toString();
        final String sphoneNumber = phoneNumber.getText().toString();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //*************We Ant using below method because it is so much slow when you have lot's of user*************
                //***********To solve this problem we are going to use Qurey
                /*for(DataSnapshot ds : dataSnapshot.child(getString(R.string.dbname_users)).getChildren()){
                    if(ds.getKey().equals(userId)){
                        user.setUsername(ds.child(userId).getValue(User.class).getUsername());
                    }
                }
                Log.d(TAG, "onDataChange: CURRENT USER " + user.getUsername());*/

                //case1 if the user made a change to their username
                //here we put not because the user typed new username so, we must have to check for uniqueness
                if(!userSettings.getUser().getUsername().equals(susername)){
                    checkIfUserNameExists(susername);
                }

                //case2 if the user made a change to their email
                if(!userSettings.getUser().getEmail().equals(semail)) {

                    // step1) Reauthenticate
                    ConfirmPasswordDialog passwordDialog = new ConfirmPasswordDialog();
                    passwordDialog.show(getFragmentManager(), getString(R.string.dialog_confirm_password));
                    passwordDialog.setTargetFragment(FragmentEditProfile.this, 1);

                    //      --Confirm the email and password


                    // step2) Check if the email already exist
                    //      --'fetchProviderForEmail(String email)'

                    // step3) change the email
                    //      --submit the email to the database and authentication

                }

                //change the rest fo the setting that don't require uniqueness
                if(!userSettings.getUserAccountSetting().getDisplay_name().equals(sdisplayName)){
                    //update the display name
                    firebaseMethods.saveChanges(sdisplayName, null, null, null);
                }
                if(!userSettings.getUserAccountSetting().getWebsite().equals(swebsite)){
                    //update the website address
                    firebaseMethods.saveChanges(null, swebsite, null, null);
                }
                if(!userSettings.getUserAccountSetting().getDescription().equals(sdescription)){
                    //update the description
                    firebaseMethods.saveChanges(null, null, sdescription, null);
                }
                if(!userSettings.getUser().getPhone_number().equals(sphoneNumber)){
                    //update the phone number
                    Log.d(TAG, "onDataChange: updating phone number : " + sphoneNumber);
                    firebaseMethods.saveChanges(null, null, null, sphoneNumber);
                }

                Toast.makeText(getActivity(), "Profile Updated.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Check @param username already exists in the database
     * @param susername
     */
    private void checkIfUserNameExists(final String susername) {
        Log.d(TAG, "checkIfUserNameExists: Checking if " + susername + " already exists.");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(susername);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //add the username
                    firebaseMethods.updateUsername(susername);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();
                }else{
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        if(singleSnapshot.exists()){
                            Log.d(TAG, "checkIfUserNameExists: FOUND A MATCH " + singleSnapshot.getValue(User.class).getUsername());
                            Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

                 Log.d(TAG, "onDataChange: dataSnapshot : " + dataSnapshot);
                //****------------RETRIEVE USER INFORMATION FROM DATBASE ----------------********//
                //You have data from database
                userSettings = firebaseMethods.getUserSetting(dataSnapshot);
                setProfileWidget(userSettings);

                //****-------------RETRIEVE IMAGE FOR THE USER IN QUESTION ---------****//
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
