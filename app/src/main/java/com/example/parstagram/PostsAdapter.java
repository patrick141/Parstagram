package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

    public void clear() {

        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView timePosted;
        private ImageView profilePic;
        private ImageView likeImage;
        private TextView likeV;
        private LinearLayout userI;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername =  itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            timePosted = itemView.findViewById(R.id.timePosted);
            profilePic = itemView.findViewById(R.id.profileImage);
            likeImage = itemView.findViewById(R.id.likeView);
            likeV = itemView.findViewById(R.id.likeC);
            userI = itemView.findViewById(R.id.userInfo);
        }

        public void bind(final Post post) {
            tvDescription.setText(post.getUser().getUsername() + ": " + post.getDesciption());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
            timePosted.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
            ParseFile userImage = post.getProfileImage();
            if(userImage != null){
                Glide.with(context).load(post.getProfileImage().getUrl()).transform(new CircleCrop()).into(profilePic);
            }
            else{
                Glide.with(context).load("").placeholder(R.drawable.instagram_user_outline_24).transform(new CircleCrop()).into(profilePic);
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
                Toast.makeText(context, " You're trying to view " + post.getUser().getUsername() + "'s details.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(post));
                context.startActivity(i);
                }
            });

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetailsActivity.class);
                    i.putExtra("user", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

        }

    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String instaFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(instaFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch ( ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
