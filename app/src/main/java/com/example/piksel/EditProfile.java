package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    private ImageView ProfilePhotoEdit;
    private String ProfilePhoto;
    private Uri ImageUri;
    private StorageReference storageReference;
    private ImageView LogoutImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar=findViewById(R.id.Toolbar2);
        BackArrow=findViewById(R.id.BackArrow);
        NameEdit=findViewById(R.id.NameEditProfile);
        EmailEdit=findViewById(R.id.EmailEditProfile);
        SaveButton=findViewById(R.id.SubmitEdit);
        ProfilePhotoEdit=findViewById(R.id.PhotoEditProfile);
        LogoutImage=findViewById(R.id.LogoutToolbar1);




        storageReference= FirebaseStorage.getInstance().getReference("Profile photos");
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
                uploadFile();
                save();
                Handler handler=new Handler();

                startActivity(new Intent(EditProfile.this,Start_activity.class));
                finish();
            }
        });



        ProfilePhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });




        LogoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EditProfile.this,MainActivity.class));
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
                    ProfilePhoto=documentSnapshot.get("Profile_photo").toString();
                    NameEdit.setText(userName);
                    EmailEdit.setText(userEmail);
                    if(!TextUtils.equals(ProfilePhoto,"-1"))
                    {
                        Glide.with(getCurrentFocus()).load(ProfilePhoto).into(ProfilePhotoEdit);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Snackbar.make(getCurrentFocus(),"Error while Loading",Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }



    private void OpenFileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.get().load(ImageUri).into(ProfilePhotoEdit);
        }
    }






    private void uploadFile()
    {
        if(ImageUri!=null)
        {
            StorageReference FileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));

            FileReference.putFile(ImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            },500);





                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String DownloadUrl=uri.toString();
                                    Snackbar.make(getCurrentFocus(),"Successfully uploaded!", Snackbar.LENGTH_SHORT).show();


                                    documentReference.update("Profile_photo",DownloadUrl);
                                }
                            });







                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            Snackbar.make(getCurrentFocus(),e.toString(), Snackbar.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                            double progress=100.00*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                        }
                    });

        }
        else
        {
            Snackbar.make(getCurrentFocus(),"No file found",Snackbar.LENGTH_SHORT).show();
        }
    }
}