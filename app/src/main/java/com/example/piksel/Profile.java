package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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

public class Profile extends AppCompatActivity {


    private ImageView BackArrow;
    private ImageView PhotoProfile;
    private Button EditProfile;
    private TextView NameProfile;
    private TextView EmailProfile;
    private Uri ImageUri;
    private String userID,userName,userEmail,ProfilePhoto;
    private DocumentReference documentReference;
    private EditText pricePhoto;
    private ProgressBar progressBarVideo;
    private StorageReference storageReference;
    private ProgressBar progressBarImage;
    private ImageView imageView;
    private FirebaseFirestore FStore=FirebaseFirestore.getInstance();
    private Button buttonUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BackArrow=findViewById(R.id.BackArrow);
        PhotoProfile=findViewById(R.id.PhotoProfile);
        EditProfile=findViewById(R.id.EditProfile);
        NameProfile=findViewById(R.id.NameProfile);
        EmailProfile=findViewById(R.id.EmailProfile);
        progressBarVideo=findViewById(R.id.Progressbar_BucketVideoProfile);
        progressBarVideo.setVisibility(View.INVISIBLE);
        pricePhoto=findViewById(R.id.PricePhotoProfile);
        storageReference=FirebaseStorage.getInstance().getReference("Photos");
        progressBarImage=findViewById(R.id.Progressbar_BucketImageProfile);
        imageView=findViewById(R.id.ImageviewUploadProfile);
        buttonUpload=findViewById(R.id.UploadButtonProfile);





        userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference=FirebaseFirestore.getInstance().collection("Users").document(userID);



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

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenFileChooser();

            }
        });



    }



    private void setValue()
    {

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    userName=documentSnapshot.get("Name").toString();
                    userEmail=documentSnapshot.get("Email").toString();
                    ProfilePhoto=documentSnapshot.get("Profile_photo").toString();
                    NameProfile.setText(userName);
                    EmailProfile.setText(userEmail);
                    if(!TextUtils.equals(ProfilePhoto,"-1"))
                    {
                        Glide.with(getCurrentFocus()).load(ProfilePhoto).into(PhotoProfile);
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



    private void uploadFile()
    {
        if(ImageUri!=null && !TextUtils.isEmpty(pricePhoto.getText().toString().trim()))
        {
            StorageReference FileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));
            progressBarImage.setVisibility(View.VISIBLE);

            FileReference.putFile(ImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarImage.setProgress(0);
                                    progressBarImage.setVisibility(View.INVISIBLE);
                                    imageView.setImageResource(R.drawable.add_photo);

                                }
                            },500);





                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String DownloadUrl=uri.toString();
                                    Snackbar.make(getCurrentFocus(),"Successfully uploaded!", Snackbar.LENGTH_SHORT).show();
                                    String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    float price=Float.valueOf(pricePhoto.getText().toString());


                                    Uploads uploads=new Uploads(DownloadUrl,user,price);
                                    FStore.collection("Photos").document().set(uploads);
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
                            progressBarImage.setProgress((int)progress);
                        }
                    });

        }
        else
        {
            Snackbar.make(getCurrentFocus(),"No file found",Snackbar.LENGTH_SHORT).show();
        }
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
            Picasso.get().load(ImageUri).into(imageView);
        }
    }
}