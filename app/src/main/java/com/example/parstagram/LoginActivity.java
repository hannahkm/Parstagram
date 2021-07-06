package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    TextView etUsername;
    TextView etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // send user straight to main activity
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }

    }

    public void SignIn(View v){
        String username = String.valueOf(etUsername.getText());
        String password = String.valueOf(etPassword.getText());

        // Invoke Log in - occurs in a background thread to improve UX
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "You're logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    // Hooray! The user is logged in.
                } else {
                    Toast.makeText(LoginActivity.this, "Check your username and password", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", String.valueOf(e));
                    return;
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    public void SignUp(View v){
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }
}
