package com.ritara.svustudent.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.Model;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private final ArrayList<Model> mValues;

    public AttendanceAdapter(ArrayList<Model> items) {
        mValues = items;
    }

    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_att, parent, false);

        return new AttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AttendanceAdapter.ViewHolder holder, int position) {
        holder.txtDate.setText(mValues.get(position).getDate());
        holder.txtAtt.setText(mValues.get(position).getAttendance());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView txtDate, txtAtt;
        public ArrayList<FeeModel> mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtAtt = (TextView) view.findViewById(R.id.txtAtt);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
        }

        @Override
        public String toString() {

            return "";
        }
    }
}
