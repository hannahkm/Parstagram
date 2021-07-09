package com.example.parstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewPostActivity extends AppCompatActivity {
    TextView etDescription;
    ImageView ivUpload;
    ImageView camera;
    Button btnMakePost;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        etDescription = findViewById(R.id.etDescription);
        ivUpload = findViewById(R.id.ivUpload);
        camera = findViewById(R.id.ivCamera);
        btnMakePost = findViewById(R.id.btnMakePost);

        // launches camera when user selects the camera
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                ivUpload.setVisibility(View.VISIBLE);
                camera.setVisibility(View.INVISIBLE);
            }
        });

        // user posts their creation
        btnMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = String.valueOf(etDescription.getText());
                if (description.isEmpty()) {
                    Toast.makeText(NewPostActivity.this, "Add a caption!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivUpload.getDrawable() == null){
                    Toast.makeText(NewPostActivity.this, "Attach a photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser user = ParseUser.getCurrentUser();
                savePost(description, user, photoFile);
            }
        });
    }

    private void launchCamera() {
        // create (implicit) Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(NewPostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // find valid camera app and go to it
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 500);

                // Configure byte output stream and compress image
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                File resizedFile = getPhotoFileUri(photoFileName + "_resized");

                try {
                    resizedFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(resizedFile);
                    // Write the bytes of the bitmap to file
                    fos.write(bytes.toByteArray());
                    fos.close();
                    ivUpload.setImageBitmap(resizedBitmap);
                    Log.d("NewPostActivity", "compressed");
                } catch (IOException e) {
                    e.printStackTrace();
                    ivUpload.setImageBitmap(rawTakenImage);
                    Log.d("NewPostActivity", "unable to compress");
                }
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NewPostActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("NewPostActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(String description, ParseUser user, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(user);
        post.setImage(new ParseFile(photoFile));

        // set the post's parameters and then save to Parse
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e("NewPost", String.valueOf(e));
                    Toast.makeText(NewPostActivity.this, "Error posting", Toast.LENGTH_SHORT).show();
                } else {
                    etDescription.setText("");
                    ivUpload.setImageResource(0);
                }
            }
        });
        finish();
    }
}