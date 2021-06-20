package com.example.piksel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class BucketSinglePhoto extends AppCompatActivity {

    private ImageView PhotoPost;
    private TextView AskedPrice, HighestBid,UserName;
    private String Url,deadLine;
    private String askedPrice, highestBid;
    private DocumentReference documentReference;
    private TextView Deadline;
    private String Name,UserId;
    private RecyclerView recyclerView;
    private int Year,Month,Day,Hour,Minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_single_photo);



        String key = getIntent().getStringExtra("key");
        documentReference = FirebaseFirestore.getInstance().collection("Photos").document(key);
        UserId= FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("Users").document(UserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Name=documentSnapshot.getString("Name");
                }
            }
        });



        PhotoPost = findViewById(R.id.PhotoPhotoPost1);
        AskedPrice = findViewById(R.id.AskedPricePhotoPost1);
        HighestBid = findViewById(R.id.HighestBidPhotoPost1);
        UserName=findViewById(R.id.USerNamePhotoPost1);
        Deadline=findViewById(R.id.PhotoPostDeadline1);
        recyclerView=findViewById(R.id.PhotoPostRecyclerView1);



        setValues();
        setBiddersData();
    }


    private void setBiddersData() {
        ArrayList<PhotoPostBidder> bidder=new ArrayList<>();

        CollectionReference collectionReference=documentReference.collection("Bidders");
        collectionReference.orderBy("Bid", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    PhotoPostBidder a=queryDocumentSnapshot.toObject(PhotoPostBidder.class);
                    bidder.add(a);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(BucketSinglePhoto.this));
                PhotoPostUserAdapter photoPostUserAdapter=new PhotoPostUserAdapter(bidder,BucketSinglePhoto.this);
                recyclerView.setAdapter(photoPostUserAdapter);

            }
        });

    }



    private void setValues() {

        if(documentReference!=null)
        {
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        Year=Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("DeadlineYear")).toString());
                        Minute=Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("DeadlineMinute")).toString());
                        Month=Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("DeadlineMonth")).toString());
                        Day=Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("DeadlineDay")).toString());
                        Hour=Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("DeadlineHour")).toString());



                        Url=documentSnapshot.getString("imageUrl");
                        deadLine=documentSnapshot.getString("deadline");
                        Glide.with(getApplicationContext()).load(Url).fitCenter().into(PhotoPost);
                        askedPrice=String.valueOf(documentSnapshot.getLong("askedPrice"));
                        AskedPrice.setText("Asked Price: $" + askedPrice);
                        highestBid = String.valueOf(documentSnapshot.getLong("highestBid"));
                        HighestBid.setText("Highest Bid: $" + highestBid);
                        Deadline.setText("DeadLine: "+deadLine);
                        String Id=documentSnapshot.getString("userID");
                        FirebaseFirestore.getInstance().collection("Users").document(Id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    String userName=documentSnapshot.getString("Name");
                                    UserName.setText(userName);

                                }
                            }
                        });
                    }
                }
            });
        }

    }
}