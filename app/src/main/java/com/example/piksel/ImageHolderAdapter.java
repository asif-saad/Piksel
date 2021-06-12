package com.example.piksel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageHolderAdapter extends RecyclerView.Adapter<ImageHolderAdapter.ImageHolder> {


    private Context context;
    private ArrayList<Uploads> uploads;
    public OnItemClickListener onItemClickListener;


    public interface OnItemClickListener
    {
        void Onclick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener)
    {
        onItemClickListener=mListener;
    }


    public ImageHolderAdapter(Context context, ArrayList<Uploads> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @NotNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.imageholder,parent,false);
        return new ImageHolder(v);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageHolder holder, int position)
    {
        Glide.with(context).load(uploads.get(position).getImageUrl()).into(holder.imageViewHolder);
        DelayFunction(500);
        holder.HighestBid.setText("Highest: "+uploads.get(position).getHighestBid());
        holder.AskedPrice.setText("Asked :"+uploads.get(position).getAskedPrice());
        FirebaseFirestore.getInstance().collection("Users").document(uploads.get(position).getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String name=documentSnapshot.get("Name").toString();
                    holder.UserName.setText(name);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return uploads.size();
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.imageholder;
    }

    public class ImageHolder extends RecyclerView.ViewHolder
    {

        public ImageView imageViewHolder;
        public TextView AskedPrice,HighestBid;
        public TextView UserName;

        public ImageHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewHolder=itemView.findViewById(R.id.ImageViewImageHolder);
            AskedPrice=itemView.findViewById(R.id.AskedPrice);
            HighestBid=itemView.findViewById(R.id.HighestBid);
            UserName=itemView.findViewById(R.id.ProfileNameImageHolder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null)
                    {
                        int position=getAdapterPosition();
                        onItemClickListener.Onclick(position);
                    }
                }
            });
        }

    }


    private void DelayFunction(int a)
    {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },a);
    }


}
