package com.example.piksel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private ArrayList<UserNotification> Notification = new ArrayList<>();
    private Context context;
    public OnItemClickListener onItemClickListener;


    public interface OnItemClickListener
    {
        void OnClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }

    public NotificationAdapter(ArrayList<UserNotification> notification, Context context) {
        Notification = notification;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.activity_notification,parent,false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationAdapter.NotificationViewHolder holder, int position) {
        holder.message.setText(Notification.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return Notification.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder
    {

        private TextView message;

        public NotificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.NotificationText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null)
                    {
                        int position=getAdapterPosition();
                        onItemClickListener.OnClick(position);
                    }
                }
            });
        }
    }
}
