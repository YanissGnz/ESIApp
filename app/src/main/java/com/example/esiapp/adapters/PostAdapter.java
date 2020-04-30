package com.example.esiapp.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;


import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.esiapp.AddPost;
import com.example.esiapp.Home;
import com.example.esiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.sql.DataSource;

import static com.example.esiapp.R.layout.row_post_item;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> mData;
    private String Uid;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
       Uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.time.setText(timestampToString((Long) mData.get(position).getimeStamp()));
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvDescription.setText(mData.get(position).getDescription());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        holder.userName.setText(mData.get(position).getUserName());
        //Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
        islike(mData.get(position).getPostKey(),holder.post_adapter_like);
        nmblike(holder.number_Likes,mData.get(position).getPostKey());
        holder.post_adapter_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.post_adapter_like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("likes").child(mData.get(position).getPostKey()).child(Uid).setValue(true);

                }
                else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("likes").child(mData.get(position).getPostKey()).child(Uid).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgPost;
        ImageView imgPostProfile,post_adapter_like;
        TextView tvDescription;
        TextView time,number_Likes;
        TextView userName;

        @RequiresApi(api = Build.VERSION_CODES.N)
        MyViewHolder(View itemView) {
            super(itemView);
           number_Likes= itemView.findViewById(R.id.like_num);
            post_adapter_like=itemView.findViewById(R.id.post_adaper_like);
            tvTitle = itemView.findViewById(R.id.post_subject);
            tvDescription = itemView.findViewById(R.id.post_description);
            imgPost = itemView.findViewById(R.id.post_picture);
            imgPostProfile = itemView.findViewById(R.id.post_profile_picture);
            imgPost.setMaxHeight(400);
            time = itemView.findViewById(R.id.post_date);
            userName = itemView.findViewById(R.id.person_name);
            imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetail.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("title", mData.get(position).getTitle());
                    postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("UserName", mData.get(position).getUserName());
                    long timestamp = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate", timestamp);
                    mContext.startActivity(postDetailActivity);
                }
            });

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetail.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("title", mData.get(position).getTitle());
                    postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("UserName", mData.get(position).getUserName());
                    long timestamp = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate", timestamp);
                    mContext.startActivity(postDetailActivity);
                }
            });



        }
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("MMM dd yyyy à HH:mm ", calendar).toString();
    }

    private void islike(String postid, final ImageView img) {
        final FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Uid).exists()) {
                    img.setImageResource(R.drawable.heart_clicked);
                    img.setTag("liked");
                } else {
                    img.setImageResource(R.drawable.heart_not_clicked);
                    img.setTag("like");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void nmblike(final TextView  likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
likes.setText(""+ dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

}
    }