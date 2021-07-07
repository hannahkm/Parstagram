package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra("Post"));
        String username = post.getUser().getUsername();
        String descrip = post.getDescription();
        String timestamp = post.getTimeStamp();
        ParseFile image = post.getImage();

        tvUsername.setText(username);
        Glide.with(this).load(image.getUrl()).into(ivImage);
        tvDescription.setText("@"+username+" "+descrip);
        tvTimestamp.setText(timestamp);


    }
}