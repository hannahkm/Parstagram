package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    RecyclerView rvUserPosts;
    List<Post> posts;
    GridPostAdapter adapter;
    TextView tvProfileName;
    ParseUser currentUser;
    ImageView ivPfp;
    SwipeRefreshLayout swipeContainer;
    Context context;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1000;
    public String photoFileName = "photo.jpg";
    private File photoFile;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = ParseUser.getCurrentUser();

        rvUserPosts = view.findViewById(R.id.rvUserPosts);
        posts = new ArrayList<>();
        adapter = new GridPostAdapter(context, posts);

        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new GridLayoutManager(context, 3));

        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileName.setText(currentUser.getUsername());

        ivPfp = view.findViewById(R.id.ivPfp);
        ParseFile currentPfp = ((ParseFile) currentUser.get("profileImage"));
        if (currentPfp != null){
            Glide.with(context).load(currentPfp.getUrl()).transform(new RoundedCornersTransformation(100, 0)).into(ivPfp);
        }

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
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

    public void changePfp(View v){
        // create (implicit) Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // find valid camera app and go to it
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NewPostActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("NewPfp", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                            Toast.makeText(context, "Your profile picture has been updated", Toast.LENGTH_SHORT).show();
                            Glide.with(context).load(newPfp.getUrl()).transform(new RoundedCornersTransformation(100, 0)).into(ivPfp);
//                            ivPfp.setImageBitmap(rawTakenImage);
                        } else {
                            Log.e("Updating pfp", String.valueOf(e));
                            Toast.makeText(context, "Error updating profile picture", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}