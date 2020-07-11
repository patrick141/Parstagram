package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

@Parcel(analyze = {Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE= "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_PROFILE_IMAGE = "ProfilePic";
    public static final String KEY_POST_LIKES = "likes";

    public String getDesciption(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getProfileImage() {
        return getUser().getParseFile("ProfilePic");
    }


    public int getLikeCount2() {
        JSONArray jsonArray = getJSONArray(KEY_POST_LIKES);
        if(jsonArray == null){
            return 0;
        }
        else{
            return jsonArray.length();
        }
    }

    public void likePost(ParseUser parseUser) {
        JSONArray jsonArray = getJSONArray(KEY_POST_LIKES);
        String temp = parseUser.getObjectId();
        if(jsonArray == null){
            add(KEY_POST_LIKES, parseUser);
            return;
        }
        else {
            boolean inArray = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    if (jsonArray.getString(i).equals(parseUser)) {
                        inArray = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!inArray) {
                add(KEY_POST_LIKES, parseUser);
            } else {
                return;
            }
        }
    }

}
