package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

//This is to call our Parse Database
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("patrick-parstagram")
                .clientKey("CodepathMoveFastParse")
                .server("https://patrick-parstagram.herokuapp.com/parse").build());
    }
}
