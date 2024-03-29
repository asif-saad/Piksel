package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Start_activity extends AppCompatActivity{

    private CircleImageView profile;
    private Toolbar toolbar;
    private ImageView LogoutImage;
    private ImageView Notification;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        profile=findViewById(R.id.Toolbar_Profile);
        toolbar=findViewById(R.id.Toolbar);
        LogoutImage=findViewById(R.id.LogoutToolbar);
        Notification=findViewById(R.id.ToolbarNotification);



        BottomNavigationView bottomNav=findViewById(R.id.BottomNavigationBar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new photos_fragment()).commit();


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_activity.this,Profile.class));
                finish();
            }
        });




        LogoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Start_activity.this,MainActivity.class));
                finish();
            }
        });





        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_activity.this,Notification.class));
            }
        });

    }


    private  BottomNavigationView.OnNavigationItemSelectedListener navListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment fragment=null;


            switch (item.getItemId())
            {
                case R.id.nav_photoLibrary:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new photos_fragment()).commit();
                    toolbar.setTitle("Photo Library");
                    return true;
                case R.id.nav_videoLibrary:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new videos_fragment()).commit();
                    toolbar.setTitle("Video Library");
                    return true;

                case R.id.nav_bucket:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new buckets_fragment()).commit();
                    toolbar.setTitle("Bucket");
                    return true;
            }
            return false;
        }
    };


}
