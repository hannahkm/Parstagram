package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton btUser;
    private RecyclerView rvPosts;
    PostsAdapter adapter;
    List<Post> feedPosts;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btUser = findViewById(R.id.btUser);
        rvPosts = findViewById(R.id.rvPosts);

        feedPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, feedPosts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });

        queryPosts();
    }

    public void fetchTimelineAsync(int page) {
        // reset the timeline with most recent 20 posts
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e("MainActivity", String.valueOf(e));
                } else {
                    for (Post post: posts){
                        Log.i("MainActivity", post.getDescription());
                    }
                    // save received posts to list and notify adapter of new data
                    feedPosts.addAll(posts);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void logout(View v){
        ParseUser.logOut();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void newPost(View v){
        Intent i = new Intent(MainActivity.this, NewPostActivity.class);
        startActivity(i);
    }
}