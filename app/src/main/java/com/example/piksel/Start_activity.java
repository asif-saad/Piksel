package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Start_activity extends AppCompatActivity{

    private CircleImageView profile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        profile=findViewById(R.id.Toolbar_Profile);


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




    }


    private  BottomNavigationView.OnNavigationItemSelectedListener navListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment fragment=null;


            switch (item.getItemId())
            {
                case R.id.nav_photoLibrary:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new photos_fragment()).commit();
                    return true;
                case R.id.nav_videoLibrary:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new videos_fragment()).commit();
                    return true;

                case R.id.nav_bucket:
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container,new buckets_fragment()).commit();
                    return true;
            }
            return false;
        }
    };


}
