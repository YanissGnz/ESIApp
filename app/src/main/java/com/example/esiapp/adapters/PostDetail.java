package com.example.esiapp.adapters;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.esiapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostDetail extends AppCompatActivity
{
    // les vues de l'activité
    ImageView postPicture, back, userPicture;
    Button AddComment;
    TextView Subject, description, UserName, PostDate, CommentText;
    EditText CommentEditText;
    RecyclerView CommentList;
    ConstraintLayout like, comment;
    CardView cardView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comment";
    CommentAdapter commentAdapter;
    String PostKey;
    //******************************************LA methode onCreate ***************************************************
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        SetViews();
        String postImageLink = Objects.requireNonNull(getIntent().getExtras()).getString("postImage");
        Glide.with(this).load(postImageLink).into(postPicture);
        // pour le theme de la publication
        String postTitle = getIntent().getExtras().getString("title");
        Subject.setText(postTitle);
        // pour la description
        String postDescription = getIntent().getExtras().getString("description");
        description.setText(postDescription);
        //pour la date
        String postusername = getIntent().getExtras().getString("UserName");
        UserName.setText(postusername);
        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        PostDate.setText(date);
        // get post id
        PostKey = getIntent().getExtras().getString("postKey");
        /* pour la photo de l'utilisateur (khelina menha)
        String userpostImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userpostImage).into(imgUserPost);*/
        AddComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //   Add_comment.setVisibility(View.INVISIBLE);
                String comment_content = CommentEditText.getText().toString().trim();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getEmail();
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                // String uimg = Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString();
                Comment comment = new Comment(comment_content, uid, uname);
                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment added");
                        CommentEditText.setText("");
                        AddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Fail to add comment : " + e.getMessage());
                    }
                });
            }
        });

       back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iniRvComment();
    }
    //********************************* Les methodes ****************************************************************
    public void SetViews() {
        postPicture = findViewById(R.id.post_detail_picture);
        Subject = findViewById(R.id.post_detail_subject);
        description = findViewById(R.id.post_detail_descreption);
        UserName = findViewById(R.id.post_detail_pesron);
        PostDate = findViewById(R.id.post_detail_date);
        CommentText = findViewById(R.id.comment_content);

        userPicture = findViewById(R.id.post_detail_profile_picture);
        userPicture.setMaxHeight(0);
        //userPicture.setVisibility(View.INVISIBLE);

        AddComment = findViewById(R.id.add_comment);
        //AddComment.setMaxHeight(0);
        //AddComment.setVisibility(View.INVISIBLE);

        CommentEditText = findViewById(R.id.post_detail_comment_text);
        CommentEditText.setMaxHeight(300);
       //CommentEditText.setVisibility(View.INVISIBLE);

        cardView = findViewById(R.id.cardView_post);

        CommentList = findViewById(R.id.rv_comment);
        back = findViewById(R.id.post_detail_back);
        comment = findViewById(R.id.comment_btn);
        like = findViewById(R.id.like_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    @NonNull
    private String timestampToString(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd MMMM yyyy", calendar).toString();
    }
    public void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void iniRvComment()
    {
        CommentList.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                Collections.reverse(listComment);
                CommentList.setAdapter(commentAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
