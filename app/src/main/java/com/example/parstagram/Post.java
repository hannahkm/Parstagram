package com.example.parstagram;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@Parcel(analyze = Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {

    public static String KEY_DESCRIPTION = "Description";
    public static String KEY_IMAGE = "Image";
    public static String KEY_USER = "User";
    public static String KEY_LIKES = "numLikes";


    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String descrip){
        put(KEY_DESCRIPTION, descrip);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }

    public int getNumLikes(){
        int likes = getInt(KEY_LIKES);
        Log.i("likes", String.valueOf(likes));
        if (likes == 0){
            int num_likes = (int)Math.floor(Math.random()*(101)+1);
            put(KEY_LIKES, num_likes);
            return num_likes;
        } else {
            return likes;
        }
    }

    public String getTimeStamp() {
        Date createdAt = this.getCreatedAt();

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}
