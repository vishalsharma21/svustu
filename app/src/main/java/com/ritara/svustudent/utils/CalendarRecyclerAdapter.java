package com.ritara.svustudent.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritara.svustudent.R;

import java.util.ArrayList;

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder> {

    private ArrayList<UserModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CalendarRecyclerAdapter(Context context, ArrayList<UserModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.info_date.setText("Date : " + mData.get(position).getDate());
        holder.info_time_in.setText("Time : " + mData.get(position).getTime());
        holder.info_message.setText(mData.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView info_date, info_time_in, info_message;
        ViewHolder(View itemView) {
            super(itemView);

            info_date = itemView.findViewById(R.id.date);
            info_time_in = itemView.findViewById(R.id.time);
            info_message = itemView.findViewById(R.id.message);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getItem(int id) {
        return mData.get(id).getDate();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
