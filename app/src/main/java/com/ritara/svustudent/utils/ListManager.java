package com.ritara.svustudent.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ListManager {
    private Context context;
    private ArrayList arrayList;
    private int resources;
    private ListManagerInterface anInterface;
    private BaseAdapterClass baseAdapterClass;
    private String for_what;

    public interface ListManagerInterface{
        void onBindView(BaseAdapterViewHolder holder, int position, String for_what);
        void holderClass(View v, String for_what);
    }

    public BaseAdapterClass getBaseAdapterClass(){
        return baseAdapterClass;
    }

    public ListManager(ListManagerInterface anInterface, Context context, ArrayList arrayList, int resources, RecyclerView recyclerView, String for_what){
        this.context = context;
        this.arrayList = arrayList;
        this.resources = resources;
        baseAdapterClass = new BaseAdapterClass();
//        baseAdapterClass.setHasStableIds(true);
        this.anInterface = anInterface;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(baseAdapterClass);
        this.for_what = for_what;
    }

    public class BaseAdapterViewHolder extends RecyclerView.ViewHolder{

        public BaseAdapterViewHolder(View itemView) {
            super(itemView);
            anInterface.holderClass(itemView, for_what);
        }
    }

    public class BaseAdapterClass extends RecyclerView.Adapter<BaseAdapterViewHolder> {

        @Override
        public BaseAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(resources, parent, false);
            BaseAdapterViewHolder holder = new BaseAdapterViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BaseAdapterViewHolder holder, int position) {
            anInterface.onBindView(holder, position, for_what);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
    }
}
