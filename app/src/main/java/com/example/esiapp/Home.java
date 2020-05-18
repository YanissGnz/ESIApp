package com.example.esiapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.esiapp.fragment.CoursesFragment;
import com.example.esiapp.fragment.HomeFragment;
import com.example.esiapp.fragment.PlannerFragment;
import com.example.esiapp.fragment.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    ImageView addPost, addDoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //Add New Post
        addPost = findViewById(R.id.addpost_button);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPost.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.none);
            }
        });

        addDoes = findViewById(R.id.add_do);
        addDoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.activity_add_to_do);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Drawer Menu and ToolBar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                addPost.setVisibility(View.VISIBLE);
                break;
            case R.id.planner:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PlannerFragment()).commit();
                addPost.setVisibility(View.INVISIBLE);
                addDoes.setVisibility(View.VISIBLE);
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                addPost.setVisibility(View.INVISIBLE);

                break;
            case R.id.nav_logout:
               // Login.logedIn = false;
                FirebaseAuth.getInstance().signOut();
                Intent loginAcivity = new Intent(getApplicationContext(), Login.class);
                startActivity(loginAcivity);
                overridePendingTransition(R.anim.slide_in_left, R.anim.none);
                finish();
                break;
            case R.id.courses:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CoursesFragment()).commit();
                addPost.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_feedback:
                Toast.makeText(this,"feedback",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd/.android.package-archive");
                intent.putExtra(Intent.EXTRA_STREAM, "my new app");
                startActivity(Intent.createChooser(intent,"ShareVia"));

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer((GravityCompat.START));
        }else {
            super.onBackPressed();
        }
    }
}
