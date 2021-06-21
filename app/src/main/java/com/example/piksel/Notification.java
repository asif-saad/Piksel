package com.example.piksel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    private ImageView Logout;
    private ImageView BackArrow;
    private String UserId;
    private ArrayList<UserNotification> array=new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        Logout=findViewById(R.id.LogoutToolbarNotification);
        BackArrow=findViewById(R.id.BackArrowNotification);
        recyclerView=findViewById(R.id.NotificationRecycler);


        UserId=FirebaseAuth.getInstance().getUid();


        CollectionReference colRef=FirebaseFirestore.getInstance().collection("Users").
                document(UserId).collection("Notifications");



        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    UserNotification a=queryDocumentSnapshot.toObject(UserNotification.class);
                    array.add(a);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(Notification.this));
                NotificationAdapter notificationAdapter=new NotificationAdapter(array,Notification.this);
                recyclerView.setAdapter(notificationAdapter);
            }
        });







        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Notification.this,MainActivity.class));
                finish();
            }
        });

        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notification.this,Start_activity.class));
                finish();
            }
        });
    }
}