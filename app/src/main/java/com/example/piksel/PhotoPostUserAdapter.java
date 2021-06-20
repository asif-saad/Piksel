package com.example.piksel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PhotoPostUserAdapter extends RecyclerView.Adapter<PhotoPostUserAdapter.PhotoPostUserAdapterViewHolder> {

    private ArrayList<PhotoPostBidder> bidders;
    private Context context;
    private int colorId;

    // user bidder recyclerview adapter

    public PhotoPostUserAdapter(ArrayList<PhotoPostBidder> bidders, Context context) {
        this.bidders = bidders;
        this.context = context;
        colorId=ContextCompat.getColor(context,R.color.Top);
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

        if(position==0)
        {
            Drawable drawable=holder.cardView.getBackground();
            drawable= DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable,colorId);
            //holder.cardView.setBackground(drawable);
            holder.cardView.setCardBackgroundColor(colorId);
        }
    }

    @Override
    public int getItemCount()
    {
        return bidders.size();
    }

    public class PhotoPostUserAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView Name,Timeline,Price;
        private CardView cardView;

        public PhotoPostUserAdapterViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            Name=itemView.findViewById(R.id.PhotoPostBidName);
            Timeline=itemView.findViewById(R.id.PhotoPostTimeline);
            Price=itemView.findViewById(R.id.PhotoPostBidPrice);
            cardView=itemView.findViewById(R.id.PhotoPostBidCardView);
        }
    }
}
