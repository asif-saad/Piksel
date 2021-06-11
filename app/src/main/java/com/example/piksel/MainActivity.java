package com.example.piksel;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    //EditText LoginEmail,LoginPassword;//SignupName,SignupEmail,SignupPassword;
    //Button Login;
    /*FirebaseFirestore FStore;
    FirebaseAuth FAuth;
    String UserID;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.viewpager);
        /*LoginEmail=findViewById(R.id.UserEmail_login);
        LoginPassword=findViewById(R.id.Login_password);*/
        /*SignupName=findViewById(R.id.Username_signup);
        SignupEmail=findViewById(R.id.UserEmail_Signup);
        SignupPassword=findViewById(R.id.Signup_password);*/
        //Login=findViewById(R.id.Login);
        /*Signup=findViewById(R.id.Signup);
        FAuth=FirebaseAuth.getInstance();
        FStore=FirebaseFirestore.getInstance();*/




        FragmentManager fm=getSupportFragmentManager();
        fragmentAdapter=new FragmentAdapter(fm,getLifecycle());



        viewPager.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });












        /*Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=SignupName.getText().toString();
                String password=SignupPassword.getText().toString();
                String email=SignupEmail.getText().toString().trim();



                FAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            UserID=FAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=FStore.collection("Users").document(UserID);

                            Map<String,Object> user=new HashMap<>();

                            user.put("Name",name);
                            user.put("Email",email);
                            user.put("Password",password);

                            documentReference.set(user);

                            Intent intent=new Intent(MainActivity.this,Start_activity.class);
                            intent.putExtra("UserID",UserID);
                            startActivity(intent);
                            finish();




                        }
                    }
                });
            }
        });*/

    }
}