package com.example.piksel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Notification extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String userId;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        recyclerView=findViewById(R.id.NotificationRecycler);
        userId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        docRef=FirebaseFirestore.getInstance().collection("Users").document(userId);


    }
}