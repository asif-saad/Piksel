package com.example.piksel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BucketOnSaleImageHolderAdapter extends RecyclerView.Adapter<BucketOnSaleImageHolderAdapter.BucketOnSaleImageHolder> {


    private ArrayList<Uploads> array;
    private Context context;
    public BucketOnItemClickListener bucketOnItemClickListener;


    public interface BucketOnItemClickListener
    {
        void OnClick(int position);
    }

    public void setBucketOnItemClickListener(BucketOnItemClickListener bucketOnItemClickListener)
    {
        this.bucketOnItemClickListener=bucketOnItemClickListener;
    }


    public BucketOnSaleImageHolderAdapter(ArrayList<Uploads> array, Context context) {
        this.array = array;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public BucketOnSaleImageHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bucketimageonsale, parent, false);

        return new BucketOnSaleImageHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull BucketOnSaleImageHolderAdapter.BucketOnSaleImageHolder holder, int position) {

        Glide.with(context).load(array.get(position).getImageUrl()).into(holder.BucketImageView);
        holder.BucketImageAskedText.setText("Asked: $" + array.get(position).getAskedPrice());
        holder.BucketImageHighestText.setText("Highest Bid: $" + array.get(position).getHighestBid());
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class BucketOnSaleImageHolder extends RecyclerView.ViewHolder {

        public TextView BucketImageAskedText, BucketImageHighestText;
        public ImageView BucketImageView;

        public BucketOnSaleImageHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            BucketImageAskedText = itemView.findViewById(R.id.BucketAskedPrice);
            BucketImageHighestText = itemView.findViewById(R.id.BucketHighestBid);
            BucketImageView = itemView.findViewById(R.id.BucketImageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bucketOnItemClickListener!=null)
                    {
                        int position=getAdapterPosition();
                        bucketOnItemClickListener.OnClick(position);
                    }
                }
            });


        }
    }
}
