package com.gsr.instagramgsr.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gsr.instagramgsr.R;

public class ConfirmPasswordDialog extends DialogFragment {
    private static final String TAG = "ConfirmPasswordDialog";

    public interface onConfirmPasswordListener{
        public void confirmPassword(String password);
    }

    onConfirmPasswordListener mConfirmPassword;

    private EditText etPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container , false);
        Log.d(TAG, "onCreateView: Started.");
        etPassword = view.findViewById(R.id.et_confirmPassword);

        TextView confirm , cancel;
        confirm = view.findViewById(R.id.tv_confirmPassword);
        cancel = view.findViewById(R.id.tv_cancelPassword);
        
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: submitting password to database and confirmation");
                String password = etPassword.getText().toString();
                if(!password.equals("")){
                    mConfirmPassword.confirmPassword(password);
                    getDialog().dismiss();
                }else {
                    Toast.makeText(getActivity(), "Must have to enter password!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Cosing the dialog.");
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mConfirmPassword = (onConfirmPasswordListener) getTargetFragment();
        }catch(ClassCastException e){
            Log.d(TAG, "ClassCastException: " + e.getMessage());
        }
    }
}
