package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ui.widget.ParseImageView;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    TextView tvUsername;
    ParseImageView ivImage;
    TextView tvDescription;
    TextView tvTimestamp;
    TextView tvNumLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvNumLikes = findViewById(R.id.tvNumLikes);

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


    }
}