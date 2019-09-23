package com.ritara.svustudent.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class  Common_RecyclerView_Adapter extends RecyclerView.Adapter<Common_RecyclerView_Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList mData;
    private String from;
    private int resource;
    private BindAdapter bindAdapter;
    private int x;

    public Common_RecyclerView_Adapter(Context mContext, BindAdapter bindAdapter) {
        this.mContext = mContext;
        this.bindAdapter = bindAdapter;
    }

    public void setListData(ArrayList mData, int resource, String name, int x) {
        this.mData = mData;
        this.resource = resource;
        this.from = name;
        this.x = x;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindAdapter.onBindHolder(holder, from, position, x);
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            if (mData.size() > 0) {
                return mData.size();
            } else {
                return 0;
            }
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            bindAdapter.onView_holder(itemView, from);
        }
    }
}

