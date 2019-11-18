package com.gsr.instagramgsr.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gsr.instagramgsr.Home.MainActivity;
import com.gsr.instagramgsr.Models.User;
import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.utils.FirebaseMethods;
import com.gsr.instagramgsr.utils.StringManiplution;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private final Context mContext = RegisterActivity.this;

    private EditText et_email, et_fullName, et_password;
    private AppCompatButton btn_register;
    private TextView tv_backToLogin;
    private RelativeLayout pb_login;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    private String semail, susername, spassword;
    private String append = "" ;

    private boolean emptyStringCheck;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started ...");

        et_email = findViewById(R.id.input_email);
        et_fullName = findViewById(R.id.input_full_name);
        et_password = findViewById(R.id.input_password);
        btn_register = findViewById(R.id.btn_register);
        tv_backToLogin = findViewById(R.id.tv_back_to_login);
        pb_login = findViewById(R.id.relativeLayout_login_progressBar);
        firebaseMethods = new FirebaseMethods(mContext);
        
        tv_backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        pb_login.setVisibility(View.GONE);
        setupFirebase();

        init();

        super.onCreate(savedInstanceState);
    }


    private boolean isEmptyString(String email, String password, String userName){
        if(!email.contains("@")){
            Toast.makeText(mContext, "Put @ symbol in email", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "isEmptyString: Put @ symbol in email");
            emptyStringCheck = true;
        }

        if(email.isEmpty() && password.isEmpty() && userName.isEmpty()){
            Toast.makeText(mContext, "All Fields must be filled!", Toast.LENGTH_SHORT).show();
            emptyStringCheck = true;
        }else if(email.isEmpty()){
            Toast.makeText(mContext, "Email is not filled!", Toast.LENGTH_SHORT).show();
            emptyStringCheck = true;
        }else if(password.isEmpty()){
            Toast.makeText(mContext, "Password is not filled!", Toast.LENGTH_SHORT).show();
            emptyStringCheck = true;
        }else if(userName.isEmpty()){
            Toast.makeText(mContext, "User Name is not filled!", Toast.LENGTH_SHORT).show();
            emptyStringCheck = true;
        }else {
            emptyStringCheck = false;
        }

        if(emptyStringCheck){
            pb_login.setVisibility(View.GONE);
        }else {
            pb_login.setVisibility(View.VISIBLE);
        }

        return emptyStringCheck;

    }


    private void init(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                semail = et_email.getText().toString();
                spassword = et_password.getText().toString();
                susername = et_fullName.getText().toString();

                //If everything goes fine
                if(!isEmptyString(semail, spassword,susername)){
                    //finish();
                    
                    pb_login.setVisibility(View.VISIBLE);
                    firebaseMethods.registerNewEmail(semail, spassword, susername);

                }else {
                    Log.d(TAG, "onClick: User is signed out : ");
                }

            }
        });
        
    }


    /*------------------Firebase Stuff------------------*/

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

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUserNameExists: FOUND A MATCH " + singleSnapshot.getValue(User.class).getUsername());
                        append = myRef.push().getKey().toString().substring(3, 10);


                        Log.d(TAG, "onDataChange: new User name : " + susername);
                    }
                }

                String mUsername = "";
                mUsername = susername + append;

                //Adding new User using Firebase method
                firebaseMethods.addNewUser(semail, mUsername, "type your description", "gsrproduction.com", "");
                Toast.makeText(mContext, "Signup successful sending verification code to mobile.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void setupFirebase(){
        Log.d(TAG, "setupFirebase: setting up firebase ");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        //Auth Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =  mAuth.getCurrentUser();
                
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: user : " + user.getUid());

                    //This will get the snapShot only Ones rather than every interval
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange: inside event listener !");

                            checkIfUserNameExists(susername);

                            //Because FireBase default signIn us we are going to signOut us first for email verification
                            mAuth.signOut();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //This will kick us back to login screen
                    finish();

                }else {
                    Log.d(TAG, "onAuthStateChanged: user is not present : ");
                }
                
            }
        };
        
        

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
