package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class EditProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView BackArrow;
    private EditText NameEdit;
    private EditText EmailEdit;
    private String userID,userName,userEmail;
    private Button SaveButton;
    private Task task;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar=findViewById(R.id.Toolbar2);
        BackArrow=findViewById(R.id.BackArrow);
        NameEdit=findViewById(R.id.NameEditProfile);
        EmailEdit=findViewById(R.id.EmailEditProfile);
        SaveButton=findViewById(R.id.SubmitEdit);



        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        task=FirebaseFirestore.getInstance().collection("Users").document(userID).get();
        documentReference=FirebaseFirestore.getInstance().collection("Users").document(userID);
        toolbar.setTitle("Edit Profile");
        SetValues();



        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userName.equals(NameEdit.getText().toString()) || !userEmail.equals(EmailEdit.getText().toString()))
                {
                    Snackbar.make(getCurrentFocus(),"Changed data has been saved!!!",Snackbar.LENGTH_SHORT).show();
                    save();
                    SetValues();
                    Handler handler=new Handler();


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    },2000);
                }
                startActivity(new Intent(EditProfile.this,Profile.class));
                finish();

            }
        });



        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                startActivity(new Intent(EditProfile.this,Start_activity.class));
                finish();
            }
        });

    }




    private void save()
    {
        if(!userName.equals(NameEdit.getText().toString()))
        {
            documentReference.update("Name",NameEdit.getText().toString());
        }
        if(!userEmail.equals(EmailEdit.getText().toString()))
        {
            documentReference.update("Email",EmailEdit.getText().toString());
        }
    }







    private void SetValues()
    {
        task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    userName=documentSnapshot.get("Name").toString();
                    userEmail=documentSnapshot.get("Email").toString();
                    NameEdit.setText(userName);
                    EmailEdit.setText(userEmail);

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