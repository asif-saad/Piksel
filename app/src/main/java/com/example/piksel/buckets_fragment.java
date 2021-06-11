package com.example.piksel;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;



public class buckets_fragment extends Fragment{


    private ImageView imageView;
    private Uri ImageUri;
    private ProgressBar progressBarImage;
    private StorageReference storageReference;
    private FirebaseFirestore FStore=FirebaseFirestore.getInstance();
    private Button buttonUpload;
    private Button Logout;
    private ProgressBar progressBarVideo;
    private EditText pricePhoto;






    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_bucket,container,false);
        imageView=root.findViewById(R.id.ImageviewUpload);
        progressBarImage=root.findViewById(R.id.Progressbar_BucketImage);
        buttonUpload=root.findViewById(R.id.UploadButton);
        progressBarImage.setVisibility(View.INVISIBLE);
        Logout=root.findViewById(R.id.Logout);
        progressBarVideo=root.findViewById(R.id.Progressbar_BucketVideo);
        progressBarVideo.setVisibility(View.INVISIBLE);
        pricePhoto=root.findViewById(R.id.PricePhoto);



        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {



        storageReference=FirebaseStorage.getInstance().getReference("Photos");
        //databaseReference=FirebaseDatabase.getInstance().getReference("Photos");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenFileChooser();

            }
        });







        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });



        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(),MainActivity.class));
                getActivity().finish();
            }
        });


    }


















    // get the file extension

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getActivity().getContentResolver();
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
                                    Snackbar.make(getView(),"Successfully uploaded!", Snackbar.LENGTH_SHORT).show();
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

                            Snackbar.make(getView(),e.toString(), Snackbar.LENGTH_SHORT).show();

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
            Snackbar.make(getView(),"No file found",Snackbar.LENGTH_SHORT).show();
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
        if(requestCode==1 && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null)
        {
            ImageUri=data.getData();
            Picasso.get().load(ImageUri).into(imageView);
        }
    }
}
