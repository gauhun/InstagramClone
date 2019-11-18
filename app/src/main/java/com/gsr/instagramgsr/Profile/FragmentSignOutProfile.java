package com.gsr.instagramgsr.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.gsr.instagramgsr.Login.LoginActivity;
import com.gsr.instagramgsr.R;

public class FragmentSignOutProfile extends Fragment {

    private static final String TAG = "FragmentSignOutProfile";
    ImageView  backarrow;
    
    AppCompatButton btnSignOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out_profile, container, false);

        Log.d(TAG, "onCreateView: setup fragment sign out profile");

        btnSignOut = view.findViewById(R.id.btn_signOut);
        
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: signing out user ...");
                
                //this will signOut the user from app
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();

                Intent intent = new Intent(getActivity(), LoginActivity.class);

                //clear the activity stack so, the user can't access the previous opened actvitiy after signOut
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            }
        });
        
        backarrow = view.findViewById(R.id.img_back_arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }
}
