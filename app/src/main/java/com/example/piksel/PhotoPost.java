package com.example.piksel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class PhotoPost extends AppCompatActivity {


    private ImageView PhotoPost;
    private TextView AskedPrice, HighestBid,UserName;
    private String Url,deadLine;
    private String askedPrice, highestBid;
    private DocumentReference documentReference;
    private EditText BidPrice;
    private Button BidButton;
    private TextView Deadline;
    private String Name,UserId,Time;
    private RecyclerView recyclerView;
    private int Year,Month,Day,Hour,Minute;
    private String HighestBidder,WinnerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post);


        String key = getIntent().getStringExtra("KeyId");
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







        PhotoPost = findViewById(R.id.PhotoPhotoPost);
        AskedPrice = findViewById(R.id.AskedPricePhotoPost);
        HighestBid = findViewById(R.id.HighestBidPhotoPost);
        UserName=findViewById(R.id.USerNamePhotoPost);
        BidPrice=findViewById(R.id.PhotoPostBidText);
        BidButton=findViewById(R.id.PhotoPostBidButton);
        Deadline=findViewById(R.id.PhotoPostDeadline);
        recyclerView=findViewById(R.id.PhotoPostRecyclerView);





        setValues();
        setBiddersData();





        BidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update the sub collection
                // update highest bid
                // add the new bid
                // update highest bidder id
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                month+=1;
                int day=cal.get(Calendar.DAY_OF_MONTH);
                int hour=cal.get(Calendar.HOUR);
                int minute=cal.get(Calendar.MINUTE);
                Time time=new Time(year,month,day,hour,minute);

                if(time.comparison(Year,Month,Day,Hour,Minute))
                {
                    documentReference.update("DeleteFlag",false);
                    Toast.makeText(PhotoPost.this,"Deadline expired!",Toast.LENGTH_SHORT).show();




                    documentReference.collection("Bidders").orderBy("Bid", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<String> user=new ArrayList<>();
                            ArrayList<String> winner=new ArrayList<>();
                            for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                            {
                                user.add(queryDocumentSnapshot.getString("UserId"));
                                winner.add(queryDocumentSnapshot.getString("Name"));
                            }

                            // getting only the unique users
                            HashSet<String> unique=new HashSet<String>(user);

                            CollectionReference colRef=FirebaseFirestore.getInstance().collection("Users");


                            // getting the highest bidder
                            // and then getting its name
                            if(user.size()>0)
                            {
                                HighestBidder=user.get(0);
                                WinnerName=winner.get(0);
                            }



                            // iterating unique bidders
                            //

                            for(String str:unique)
                            {
                                Map<String,Object> map=new HashMap<>();
                                if(str==HighestBidder)
                                {
                                    map.put("message","Congratulations!! You have the bidding. Tap to view the Bidding");
                                }
                                else
                                {
                                    map.put("message",WinnerName+" has won the bidding auction. Tap to view the Bidding");
                                }
                                map.put("index",System.currentTimeMillis());
                                //map.put("timestamp",Timestamp.now());
                                map.put("PhotoId",key);
                                colRef.document(str).collection("Notifications").document().set(map);
                            }
                        }
                    });

                }
                else if(Integer.parseInt(BidPrice.getText().toString().trim())>Integer.parseInt(highestBid))
                {
                    highestBid=BidPrice.getText().toString();
                    HighestBid.setText("Highest Bid: $"+highestBid);
                    BidPrice.getText().clear();

                    Map<String,Object> map= new HashMap<>();
                    Time= cal.get(Calendar.DAY_OF_MONTH) +" "+getMonth(cal.get(Calendar.MONTH))+" "+cal.get(Calendar.YEAR)+" at "+
                            cal.get(Calendar.HOUR)+":"+ cal.get(Calendar.MINUTE);
                    map.put("Name",Name);
                    map.put("Bid",Integer.parseInt(highestBid));
                    map.put("Time",Time);
                    map.put("UserId",UserId);



                    documentReference.collection("Bidders").document().set(map);
                    Snackbar.make(getCurrentFocus(),"Bid has been updated!",Snackbar.LENGTH_SHORT).show();


                    documentReference.update("highestBid",Integer.parseInt(highestBid));
                    documentReference.update("HighestID",UserId);
                    setBiddersData();

                }
                else
                {
                    Snackbar.make(getCurrentFocus(),"Highest Bid: $"+highestBid,Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }


    //  setting the bidders bidding info in the scrollview s recyclerview

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
                recyclerView.setLayoutManager(new LinearLayoutManager(PhotoPost.this));
                PhotoPostUserAdapter photoPostUserAdapter=new PhotoPostUserAdapter(bidder,PhotoPost.this);
                recyclerView.setAdapter(photoPostUserAdapter);

            }
        });

    }




    //  getting the name of month by passing their numerical order in the year

    private String getMonth(int month)
    {
        month+=1;
        switch (month)
        {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";

            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return null;
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