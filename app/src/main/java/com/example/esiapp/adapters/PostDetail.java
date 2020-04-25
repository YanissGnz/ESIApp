package com.example.esiapp.adapters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.esiapp.R;

public class PostDetail extends AppCompatActivity {

    ImageView postPicture;
    EditText commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postPicture = findViewById(R.id.post_detail_picture);
        postPicture.setMaxHeight(500);

        commentText = findViewById(R.id.post_detail_comment_text);
        commentText.setMaxHeight(300);
    }
}
