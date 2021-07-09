package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up fragment manager to switch between pages
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment homeFragment = new DashboardFragment();
        final Fragment postFragment = new NewPostFragment();
        final Fragment userFragment = new UserProfileFragment();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // switch to appropriate fragment onClick in the navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.btHome:
                        fragment = homeFragment;
                        break;
                    case R.id.btPost:
                        fragment = postFragment;
                        break;
                    case R.id.btUser:
                        fragment = userFragment;
                        break;
                    default: return true;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });

        // sets the default page (the dashboard)
        bottomNavigationView.setSelectedItemId(R.id.btHome);
    }
}