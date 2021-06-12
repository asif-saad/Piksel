package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class PhotoPost extends AppCompatActivity {


    private ImageView PhotoPost;
    private ImageView ProfilePhoto;
    private TextView AskedPrice, HighestBid;
    private String Url;
    private String askedPrice, highestBid;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post);


        String key = getIntent().getStringExtra("KeyId");
        documentReference = FirebaseFirestore.getInstance().collection("Photos").document(key);


        PhotoPost = findViewById(R.id.PhotoPhotoPost);
        AskedPrice = findViewById(R.id.AskedPricePhotoPost);
        HighestBid = findViewById(R.id.HighestBidPhotoPost);


        setValues();

    }

    private void setValues() {

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Url = documentSnapshot.get("imageUrl").toString();
                            Glide.with(getCurrentFocus()).load(Url).into(PhotoPost);
                            askedPrice = documentSnapshot.get("askedPrice").toString();
                            AskedPrice.setText("Asked Price: " + askedPrice);

                            highestBid = documentSnapshot.get("highestBid").toString();
                            HighestBid.setText("Highest Bid: " + highestBid);
                        }
                    }
                });
            }
        }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}