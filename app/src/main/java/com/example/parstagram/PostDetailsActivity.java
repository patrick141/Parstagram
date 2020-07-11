package com.example.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import static com.example.parstagram.PostsAdapter.getRelativeTimeAgo;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;
    public static final String TAG = "PostDetailsActivity";
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView timePosted;
    private ImageView profilePic;
    private ImageView likeImage;
    private TextView likeV;
    private LinearLayout userI;
    private Button followBotton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        tvUsername =  findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        timePosted = findViewById(R.id.timePosted);
        profilePic = findViewById(R.id.profileImage);
        likeImage = findViewById(R.id.likeView);
        likeV = findViewById(R.id.likeC);
        userI = findViewById(R.id.userInfo);

        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getUser().getUsername() + ": " + post.getDesciption());
        tvUsername.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        }
        timePosted.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        ParseFile userImage = post.getProfileImage();
        if(userImage != null){
            profilePic.setVisibility(View.VISIBLE);
            System.out.println(post.getProfileImage().getUrl());
            Glide.with(this).load(post.getProfileImage().getUrl()).transform(new CircleCrop()).into(profilePic);
        }
        else{
            profilePic.setVisibility(View.GONE);
        }
        likeV.setText(post.getLikeCount2() + " ");

        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, " You liked it ", Toast.LENGTH_SHORT).show();
                post.likePost(ParseUser.getCurrentUser());
                post.saveInBackground();
                likeV.setText(post.getLikeCount2() + " ");
            }
        });

        userI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailsActivity.this, " You're trying to view " + post.getUser().getUsername() + "'s details.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PostDetailsActivity.this, ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(post));
                PostDetailsActivity.this.startActivity(i);
            }
        });
    }
}