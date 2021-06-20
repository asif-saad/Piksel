package com.example.piksel;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class photos_fragment extends Fragment {

    private FirebaseFirestore FStore = FirebaseFirestore.getInstance();
    private CollectionReference PhotoRef;
    private ArrayList<Uploads> Array=new ArrayList<>();
    private ArrayList<String> key=new ArrayList<>();
    private RecyclerView recyclerView;
    public ImageHolderAdapter imageHolderAdapter;
    private int Year,Month,Day,Hour,Minute;




    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_photos, container, false);
        recyclerView=root.findViewById(R.id.Photos_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));




        PhotoRef=FStore.collection("Photos");

        Calendar cal=Calendar.getInstance();
        Year=cal.get(Calendar.YEAR);
        Month=cal.get(Calendar.MONTH);
        Month+=1;
        Day=cal.get(Calendar.DAY_OF_MONTH);
        Hour=cal.get(Calendar.HOUR);
        Minute=cal.get(Calendar.MINUTE);





        /*Query query=PhotoRef.limit(3);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Uploads productModel = document.toObject(Uploads.class);
                        Array.add(productModel.getImageUrl());
                    }
                    productAdapter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;
                                Query nextQuery = productsRef.orderBy("productName", Query.Direction.ASCENDING).startAfter(lastVisible).limit(4);
                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                        if (t.isSuccessful()) {
                                            for (DocumentSnapshot d : t.getResult()) {
                                                Uploads productModel = d.toObject(Uploads.class);
                                                Array.add(productModel.getImageUrl());
                                            }
                                            productAdapter.notifyDataSetChanged();
                                            lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                                            if (t.getResult().size() < 4) {
                                                isLastItemReached = true;
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    };
                    recyclerView.addOnScrollListener(onScrollListener);
                }
            }
        });*/









        PhotoRef.whereEqualTo("DeleteFlag",true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {
                    String c= Objects.requireNonNull(queryDocumentSnapshot.getData().get("userID")).toString();
                    String s= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    if(!c.equals(s))
                    {
                        Uploads a=queryDocumentSnapshot.toObject(Uploads.class);
                        key.add(queryDocumentSnapshot.getId());
                        Array.add(a);
                    }


                }
                imageHolderAdapter=new ImageHolderAdapter(root.getContext(),Array);
                recyclerView.setAdapter(imageHolderAdapter);
                imageHolderAdapter.setOnItemClickListener(new ImageHolderAdapter.OnItemClickListener() {
                    @Override
                    public void Onclick(int position) {
                        Intent intent=new Intent(getContext(),PhotoPost.class);
                        intent.putExtra("KeyId",key.get(position));
                        startActivity(intent);
                    }
                });
            }



        });







        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {

    }
}
