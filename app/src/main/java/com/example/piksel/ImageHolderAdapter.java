package com.example.piksel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageHolderAdapter extends RecyclerView.Adapter<ImageHolderAdapter.ImageHolder> {


    private Context context;
    private ArrayList<Uploads> uploads;


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



    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageHolder holder, int position)
    {
        Glide.with(context).load(uploads.get(position).getImageUrl()).into(holder.imageViewHolder);
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

        public ImageHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewHolder=itemView.findViewById(R.id.ImageViewImageHolder);
        }

    }


}
