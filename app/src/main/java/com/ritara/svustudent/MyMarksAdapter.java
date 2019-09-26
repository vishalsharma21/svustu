package com.ritara.svustudent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ritara.svustudent.fragments.MarksFragment;
import com.ritara.svustudent.fragments.PaidFeeFragment;
import com.ritara.svustudent.utils.FeeModel;

import java.util.ArrayList;

public class MyMarksAdapter extends RecyclerView.Adapter<MyMarksAdapter.ViewHolder> {

    private final ArrayList<FeeModel> mValues;
    private final MarksFragment.OnListFragmentInteractionListener mListener;

    public MyMarksAdapter(ArrayList<FeeModel> items, MarksFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public MyMarksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_marks, parent, false);

        return new MyMarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyMarksAdapter.ViewHolder holder, int position) {
        holder.txtMarks.setText("Marks : " + mValues.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView txtMarks;
        public ArrayList<FeeModel> mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtMarks = (TextView) view.findViewById(R.id.txtMarks);

        }

        @Override
        public String toString() {

            return "";
        }
    }
}
