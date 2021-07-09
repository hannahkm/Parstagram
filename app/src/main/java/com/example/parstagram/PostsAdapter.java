package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.parse.ui.widget.ParseImageView;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ParseImageView ivImage;
        TextView tvDescription;
        TextView tvNumLikes;
        ImageView ivUserPfp;
        TextView tvTimestamp;
        ImageView ivLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
            ivUserPfp = itemView.findViewById(R.id.ivUserPfp);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivLike = itemView.findViewById(R.id.ivLike);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    Post sendingPost = posts.get(getAdapterPosition());
                    i.putExtra("Post", Parcels.wrap(sendingPost));
                    i.putExtra("numLikes", sendingPost.getNumLikes());
                    context.startActivity(i);
                }
            });

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post liked = posts.get(getAdapterPosition());
                    int numLikes = liked.getNumLikes();
                    if (liked.getBoolean("liked")) {
                        ivLike.setImageResource(R.drawable.heart_stroke);
                        numLikes -= 1;
                        liked.put("liked", false);
                    } else {
                        ivLike.setImageResource(R.drawable.heart_active);
                        numLikes += 1;
                        liked.put("liked", true);
                    }
                    liked.put("numLikes", numLikes);
                    tvNumLikes.setText(String.valueOf(numLikes) + " likes");

                    liked.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null){
                                Log.e("NewPost", String.valueOf(e));
                                Toast.makeText(context, "Error liking this post", Toast.LENGTH_SHORT).show();
                            } else {
                                return;
                            }
                        }
                    });
                }
            });

        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            String username = post.getUser().getUsername();
            String description = "<b>" + username + "</b> " + post.getDescription();
            tvDescription.setText(Html.fromHtml(description));
            tvUsername.setText(username);
            tvNumLikes.setText(post.getNumLikes() + " likes");
            tvTimestamp.setText(post.getTimeStamp());
            ParseFile image = post.getImage();
            if (image != null) {
//                ivImage.setParseFile(image);
//                ivImage.loadInBackground();
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            ParseFile currentPfp = ((ParseFile) post.getUser().get("profileImage"));
            if (currentPfp != null){
                Glide.with(context).load(currentPfp.getUrl()).override(100, 100).circleCrop().into(ivUserPfp);
            }

            if (post.getBoolean("liked")) {
                ivLike.setImageResource(R.drawable.heart_active);
            }
        }
    }
}