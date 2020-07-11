package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";

    Post post;
    PostsAdapter adapter;
    RecyclerView rvThis;
    ImageView ivProfile;
    TextView tvUsername;
    TextView otherName;
    TextView followeC;
    TextView followingC;
    public SwipeRefreshLayout swipeContainer;
    List<Post> thisUserPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        Log.d(TAG, "Showing profile details for " + post.getUser().getUsername());
        ivProfile = findViewById(R.id.profilePA);
        //tvUsername = findViewById(R.id.screennamePA);
        otherName = findViewById(R.id.usernamePA);
        followeC = findViewById(R.id.followerC);
        followingC = findViewById(R.id.followingC);
        thisUserPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, thisUserPosts);
        rvThis = findViewById(R.id.rvPosts);
        rvThis.setAdapter(adapter);
        rvThis.setLayoutManager(new LinearLayoutManager(this));
        //rvThis.setLayoutManager(gridLayoutManager);
        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //tvUsername.setText(post.getUser().getUsername());
        ParseFile userImage = post.getProfileImage();
        if(userImage != null){
            //ivProfile.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.getProfileImage().getUrl()).transform(new CircleCrop()).into(ivProfile);
        }
        else{
            Glide.with(this).load("").placeholder(R.drawable.instagram_user_outline_24).transform(new CircleCrop()).into(ivProfile);
        }
        otherName.setText("@"  + post.getUser().getUsername());
        queryPosts();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                queryPosts();
            }
        });

        //followeC.setText(post.getUser().getJSONArray("followers").length()+ " ");
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, post.getUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts){
                    Log.i(TAG, "Post: " + post.getDesciption() + " Username: " + post.getUser().getUsername());
                }
                adapter.clear();
                adapter.addAll(posts);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
}