package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static String KEY_DESCRIPTION = "Description";
    public static String KEY_IMAGE = "Image";
    public static String KEY_USER = "User";


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
}
