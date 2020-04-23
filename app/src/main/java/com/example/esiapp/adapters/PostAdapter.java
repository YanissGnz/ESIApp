package com.example.esiapp.adapters;


import android.content.Context;
import android.content.Intent;


import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.esiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> mData ;
    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time.setText(timestampToString((Long)mData.get(position).getimeStamp()));
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvDescription.setText(mData.get(position).getDescription());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        holder.userName.setText(mData.get(position).getUserName());

        //Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle;
        ImageView imgPost;
        ImageView imgPostProfile;
        TextView tvDescription;
         TextView time;
         TextView userName;

        @RequiresApi(api = Build.VERSION_CODES.N)
        MyViewHolder(View itemView)
        {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.post_subject);
            tvDescription=itemView.findViewById(R.id.post_description);
            imgPost = itemView.findViewById(R.id.post_picture);
            imgPostProfile = itemView.findViewById(R.id.profile_picture);
            imgPost.setMaxHeight(400);
            time = itemView.findViewById(R.id.post_date);
            userName= itemView.findViewById(R.id.person_name);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetail.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("title",mData.get(position).getTitle());
                    postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                    postDetailActivity.putExtra("description",mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                  //  postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                 //   postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timestamp) ;
                    mContext.startActivity(postDetailActivity);
                }
            });

        }


    }
    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd MMMM yyyy à HH:mm ",calendar).toString();
    }



}