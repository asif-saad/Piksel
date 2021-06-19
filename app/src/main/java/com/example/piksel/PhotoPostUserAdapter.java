package com.example.piksel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PhotoPostUserAdapter extends RecyclerView.Adapter<PhotoPostUserAdapter.PhotoPostUserAdapterViewHolder> {

    private ArrayList<PhotoPostBidder> bidders;
    private Context context;

    public PhotoPostUserAdapter(ArrayList<PhotoPostBidder> bidders, Context context) {
        this.bidders = bidders;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PhotoPostUserAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.photopostbid,parent,false);
        return new PhotoPostUserAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PhotoPostUserAdapter.PhotoPostUserAdapterViewHolder holder, int position) {
        holder.Name.setText(bidders.get(position).getName());
        holder.Price.setText("Bid: $"+bidders.get(position).getBid());
        holder.Timeline.setText(bidders.get(position).getTime());
    }

    @Override
    public int getItemCount()
    {
        return bidders.size();
    }

    public class PhotoPostUserAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView Name,Timeline,Price;

        public PhotoPostUserAdapterViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            Name=itemView.findViewById(R.id.PhotoPostBidName);
            Timeline=itemView.findViewById(R.id.PhotoPostTimeline);
            Price=itemView.findViewById(R.id.PhotoPostBidPrice);
        }
    }
}
