package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView rvUserPosts;
    List<Post> posts;
    GridPostAdapter adapter;
    TextView tvProfileName;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = ParseUser.getCurrentUser();

        rvUserPosts = findViewById(R.id.rvUserPosts);
        posts = new ArrayList<>();
        adapter = new GridPostAdapter(this, posts);

        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new GridLayoutManager(this, 3));

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileName.setText(currentUser.getUsername());

        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> userPosts, ParseException e) {
                if (e != null){
                    Log.e("MainActivity", String.valueOf(e));
                } else {
                    for (Post post: userPosts){
                        Log.i("MainActivity", String.valueOf(post.getUser().getUsername()));
                        if (post.getUser().getUsername().equals(currentUser.getUsername())){
                            Log.i("added", String.valueOf(post.getUser()));
                            posts.add(post);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void mainPage(View v){
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void newPost(View v){
        Intent i = new Intent(ProfileActivity.this, NewPostActivity.class);
        startActivity(i);
    }

    public void logout(View v){
        ParseUser.logOut();
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void changePfp(View v){
        // stuff here
    }
}