package com.example.esiapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.esiapp.R;

public class CoursesFragment extends Fragment {

    Button cpi1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        view.findViewById(R.id.courses_1cpi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked("https://drive.google.com/open?id=1AkZbmItOEPOknyVQQi1eWhp3nTIrXYD0");
            }
        });

        view.findViewById(R.id.courses_2cpi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked("https://drive.google.com/open?id=1VICy76n33REM0JiiBC-RpG57Xv2DDLsy");
            }
        });

        view.findViewById(R.id.courses_1sc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked("https://drive.google.com/folderview?id=0B6ANTtDVbRt4TDJJYVQtdlh5LTQ");
            }
        });

        view.findViewById(R.id.courses_2sc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked("https://drive.google.com/folderview?id=0B6ANTtDVbRt4X09PRkZieHpxN2c");
            }
        });

        view.findViewById(R.id.courses_3sc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked("https://drive.google.com/folderview?id=0B6ANTtDVbRt4UmZQc1gzZFZWdk0");
            }
        });



        return view;
    }


    public void clicked(String url){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData((Uri.parse(url)));
        startActivity(intent);
    }
}
