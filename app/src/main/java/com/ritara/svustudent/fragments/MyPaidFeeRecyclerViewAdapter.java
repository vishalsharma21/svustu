package com.ritara.svustudent.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritara.svustudent.R;
import com.ritara.svustudent.fragments.PaidFeeFragment.OnListFragmentInteractionListener;
import com.ritara.svustudent.fragments.dummy.DummyContent.DummyItem;
import com.ritara.svustudent.utils.FeeModel;

import java.util.ArrayList;
import java.util.List;

public class MyPaidFeeRecyclerViewAdapter extends RecyclerView.Adapter<MyPaidFeeRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<FeeModel> mValues;

    public MyPaidFeeRecyclerViewAdapter(ArrayList<FeeModel> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paidfee, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item_Branch.setText("Branch : " + mValues.get(position).getBranch());
        holder.item_Course.setText("Counrse : " + mValues.get(position).getCourse());
        holder.item_TransDate.setText("Transaction Dt : " + mValues.get(position).getTransDate());
        holder.item_ReceiptNo.setText("Receipt No. : " + mValues.get(position).getReceiptNo());
        holder.item_Name.setText("Name : " + mValues.get(position).getName());
        holder.item_FName.setText("Fathers Name : " + mValues.get(position).getFName());
        holder.item_EnrollNo.setText("Enrollment No. : " + mValues.get(position).getEnrollNo());
        holder.item_Description.setText("Description : " + mValues.get(position).getDescription());
        holder.item_Debit.setText("Debit : " + mValues.get(position).getDebit());
        holder.item_Credit.setText("Credit : " + mValues.get(position).getCredit());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView item_Branch, item_Course, item_Credit, item_Debit, item_Description, item_EnrollNo, item_FName, item_Name, item_ReceiptNo, item_TransDate;
        public ArrayList<FeeModel> mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            item_Branch = (TextView) view.findViewById(R.id.item_Branch);
            item_Course = (TextView) view.findViewById(R.id.item_Course);
            item_Credit = (TextView) view.findViewById(R.id.item_Credit);
            item_Debit = (TextView) view.findViewById(R.id.item_Debit);
            item_Description = (TextView) view.findViewById(R.id.item_Description);
            item_EnrollNo = (TextView) view.findViewById(R.id.item_EnrollNo);
            item_FName = (TextView) view.findViewById(R.id.item_FName);
            item_Name = (TextView) view.findViewById(R.id.item_Name);
            item_ReceiptNo = (TextView) view.findViewById(R.id.item_ReceiptNo);
            item_TransDate = (TextView) view.findViewById(R.id.item_TransDate);
        }

        @Override
        public String toString() {

            return "";
        }
    }
}
