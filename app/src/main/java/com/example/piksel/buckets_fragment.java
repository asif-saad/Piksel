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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class buckets_fragment extends Fragment{


    private FirebaseFirestore FStore=FirebaseFirestore.getInstance();
    private String UserId;
    public ArrayList<Uploads> PhotosArray=new ArrayList<>();
    private RecyclerView recyclerView;
    private BucketOnSaleImageHolderAdapter bucketOnSaleImageHolderAdapter;







    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_bucket,container,false);
        recyclerView=root.findViewById(R.id.BucketOnSaleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





        CollectionReference collectionReference=FStore.collection("Photos");
        UserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        collectionReference.whereEqualTo("userID",UserId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    PhotosArray.add(documentSnapshot.toObject(Uploads.class));
                }

                bucketOnSaleImageHolderAdapter=new BucketOnSaleImageHolderAdapter(PhotosArray,getContext());
                recyclerView.setAdapter(bucketOnSaleImageHolderAdapter);
            }
        }).
                addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });


        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {



    }


}
