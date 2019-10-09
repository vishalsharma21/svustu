package com.ritara.svustudent;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ritara.svustudent.fragments.MarksFragment;
import com.ritara.svustudent.fragments.PaidFeeFragment;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class MyMarksAdapter extends RecyclerView.Adapter<MyMarksAdapter.ViewHolder> {

    private final ArrayList<FeeModel> mValues;
    private Context context;
    private SharedPreferences_SVU sharedPreferences_svu;

    public MyMarksAdapter(ArrayList<FeeModel> items, Context context) {
        mValues = items;
        this.context = context;
        sharedPreferences_svu = SharedPreferences_SVU.getInstance(context);
    }

    @Override
    public MyMarksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_marks, parent, false);

        return new MyMarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyMarksAdapter.ViewHolder holder, final int position) {
        holder.txtMarks.setText(mValues.get(position).getName());

        holder.txtMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultDialog(mValues.get(position).getName());
            }
        });

    }

    private void showResultDialog(String name) {
        //Marks : IVth  Sem
        String[] marks = name.split(":");
        String mrks = marks[1].trim();
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_result_view);

        WebView webView = dialog.findViewById(R.id.wvResult);
        //http://192.168.2.100:84/PrintStatus.aspx?EnrollNo=SOL17A05010014&Sem=Ist%20%20Sem

        ​​​​​​​​WebSettings webSettings = webView.getSettings(); ​​
        ​​​​​​webSettings.setBuiltInZoomControls(true);
        ​​​​​​​​webView.loadUrl("http://192.168.2.100:84/PrintStatus.aspx?EnrollNo="+sharedPreferences_svu.getUserId()+"&Sem="+mrks);

        dialog.show();
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
