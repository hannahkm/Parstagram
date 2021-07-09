package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    TextView etUsername;
    TextView etPassword;
    TextView etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
    }

    public void SignUp(View v){
        // user inputs username, password, and email to sign up
        String username = String.valueOf(etUsername.getText());
        String password = String.valueOf(etPassword.getText());
        String email = String.valueOf(etEmail.getText());

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Invoke signUpInBackground - adds user to Parse
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "You're signed up!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(i);
                    // Hooray! Let them use the app now.
                } else {
                    Toast.makeText(SignUpActivity.this, "There was a problem.", Toast.LENGTH_SHORT).show();
                    Log.e("SignUpActivity", String.valueOf(e));
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });

    }
}