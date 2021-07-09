package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ui.widget.ParseImageView;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {
    TextView tvUsername;
    ParseImageView ivImage;
    TextView tvDescription;
    TextView tvTimestamp;
    TextView tvNumLikes;
    ImageView ivUserPfp;
    ImageView ivLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvNumLikes = findViewById(R.id.tvNumLikes);
        ivUserPfp = findViewById(R.id.ivUserPfp);
        ivLike = findViewById(R.id.ivLike);

        // grabbing all important information from Parcelled post variable
        Post post = Parcels.unwrap(getIntent().getParcelableExtra("Post"));
        int numLikes = getIntent().getIntExtra("numLikes", 0);
        String username = post.getUser().getUsername();
        String descrip = post.getDescription();
        String timestamp = post.getTimeStamp();
        ParseFile image = post.getImage();

        tvUsername.setText(username);
        Glide.with(this).load(image.getUrl()).into(ivImage);
        String description = "<b>" + username + "</b> " + descrip;
        tvDescription.setText(Html.fromHtml(description));
        tvTimestamp.setText(timestamp);
        tvNumLikes.setText(numLikes + " likes");

        // if the user has set a profile picture, display it. Else, display default image
        ParseFile currentPfp = ((ParseFile) post.getUser().get("profileImage"));
        if (currentPfp != null){
            Glide.with(this).load(currentPfp.getUrl()).circleCrop().into(ivUserPfp);
        }

        if (post.getBoolean("liked")) {
            ivLike.setImageResource(R.drawable.heart_active);
        }


    }
}