package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parstagram.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends LoginActivity {

    public static final String TAG = "SignUpActivity";
    EditText newUsername;
    EditText newPassword;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        newUsername = binding.newUsername;
        newPassword = binding.newPassword;
        btnSignUp = binding.btnCreate;

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                final String username = newUsername.getText().toString();
                final String password = newPassword.getText().toString();
                Toast.makeText(SignUpActivity.this, " Signing up", Toast.LENGTH_SHORT).show();

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            loginUser(username, password);
                        } else {
                            Log.e(TAG, " failure on creating new account");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}