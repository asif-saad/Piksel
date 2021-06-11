package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

public class Profile extends AppCompatActivity {


    private ImageView BackArrow;
    private ImageView PhotoProfile;
    private Button EditProfile;
    private TextView NameProfile;
    private TextView EmailProfile;
    private String userID,userName,userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BackArrow=findViewById(R.id.BackArrow);
        PhotoProfile=findViewById(R.id.PhotoProfile);
        EditProfile=findViewById(R.id.EditProfile);
        NameProfile=findViewById(R.id.NameProfile);
        EmailProfile=findViewById(R.id.EmailProfile);


        setValue();







        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Start_activity.class));
                finish();
            }
        });


        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,EditProfile.class));
                finish();
            }
        });





    }



    private void setValue()
    {

        userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    userName=documentSnapshot.get("Name").toString();
                    userEmail=documentSnapshot.get("Email").toString();
                    NameProfile.setText(userName);
                    EmailProfile.setText(userEmail);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Snackbar.make(getCurrentFocus(),"Error while Loading",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}