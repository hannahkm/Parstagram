package com.example.parstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView rvUserPosts;
    List<Post> posts;
    GridPostAdapter adapter;
    TextView tvProfileName;
    ParseUser currentUser;
    ImageView ivPfp;
    SwipeRefreshLayout swipeContainer;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
    public String photoFileName = "photo.jpg";
    private File photoFile;

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

        ivPfp = findViewById(R.id.ivPfp);
        ParseFile currentPfp = ((ParseFile) currentUser.get("profileImage"));
        if (currentPfp != null){
            Glide.with(ProfileActivity.this).load(currentPfp.getUrl()).transform(new RoundedCornersTransformation(100, 0)).into(ivPfp);
        }

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
        // create (implicit) Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(ProfileActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // find valid camera app and go to it
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NewPostActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("NewPfp", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                File imageFile = getPhotoFileUri(photoFileName);
                ParseFile newPfp = new ParseFile(imageFile);
                currentUser.put("profileImage", newPfp);

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Log.i("Updating pfp", "hello updated");
                            Toast.makeText(ProfileActivity.this, "Your profile picture has been updated", Toast.LENGTH_SHORT).show();
                            Glide.with(ProfileActivity.this).load(newPfp.getUrl()).transform(new RoundedCornersTransformation(100, 0)).into(ivPfp);
//                            ivPfp.setImageBitmap(rawTakenImage);
                        } else {
                            Log.e("Updating pfp", String.valueOf(e));
                            Toast.makeText(ProfileActivity.this, "Error updating profile picture", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}