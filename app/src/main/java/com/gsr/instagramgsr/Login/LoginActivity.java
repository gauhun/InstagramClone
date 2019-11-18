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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gsr.instagramgsr.Home.MainActivity;
import com.gsr.instagramgsr.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private final Context mContext = LoginActivity.this;
    private FirebaseAuth mAuth;

    private EditText et_email, et_password;
    private AppCompatButton btn_login;
    private TextView tv_registerText;
    private RelativeLayout pb_login;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started ...");

        et_email = findViewById(R.id.input_email);
        et_password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        tv_registerText = findViewById(R.id.tv_create_new_account);
        pb_login = findViewById(R.id.relativeLayout_login_progressBar);

        pb_login.setVisibility(View.GONE);

        setupFirebase();

        init();

        super.onCreate(savedInstanceState);
    }


    //------------------Check for empty strings------------------
    private boolean checkEmailPassword(String email, String password){
        if(email.isEmpty() && password.isEmpty()){
            Toast.makeText(mContext, "All Fields must be filled!", Toast.LENGTH_SHORT).show();
            return true;
        }else if(email.isEmpty()){
            Toast.makeText(mContext, "Email is not filled!", Toast.LENGTH_SHORT).show();
            return true;
        }else if(password.isEmpty()){
            Toast.makeText(mContext, "Password is not filled!", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return false;
        }
    }

    //------------------------Initializing listeners----------------
    private void init(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail = et_email.getText().toString();
                String spassword = et_password.getText().toString();

                //If everything goes fine
                if(!checkEmailPassword(semail, spassword)){
                    loginUser(semail, spassword);
                    pb_login.setVisibility(View.VISIBLE);
                }


            }
        });
        
        tv_registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: jumping to register activity ...");
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });
    }


    /* --------------------------- Firebase Stuff -----------------------------*/

    private void setupFirebase(){
        Log.d(TAG, "setupFirebase: setting up firebase ");
        mAuth = FirebaseAuth.getInstance();

    }


    private void loginUser(String email, String password){

        //Double security for firebase default singIn
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            //check for email is verified
                            try{
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user.isEmailVerified()){
                                    Log.d(TAG, "onComplete: email is verified!");

                                    //Sign in success
                                    Log.d(TAG, "onComplete: signInWithEmail: " + getString(R.string.auth_success));
                                    startActivity(new Intent(mContext, MainActivity.class));
                                }

                            }catch(NullPointerException e){
                                Log.d(TAG, "onComplete: NullPointerException " + e.getMessage());
                            }


                        }else {
                            //If sign in fail display the message to user
                            Log.d(TAG, "onComplete: login unsuccesful!");
                            Toast.makeText(mContext, getString(R.string.auth_failed) + "!", Toast.LENGTH_SHORT).show();
                            et_email.setText("");
                            et_password.setText("");
                        }

                        pb_login.setVisibility(View.GONE);
                    }
                });
    }
}
