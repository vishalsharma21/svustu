package com.ritara.svustudent.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final ArrayList<Model> mValues;
    private Context activity;


    public GalleryAdapter(ArrayList<Model> items, FragmentActivity activity) {
        mValues = items;
        this.activity = activity;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gal, parent, false);

        return new GalleryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {
        Picasso.get()
                .load(mValues.get(position).getImage()) // Equivalent of what ends up in onBitmapLoaded
                .placeholder(R.drawable.app_icon) // Equivalent of what ends up in onPrepareLoad
                .error(R.drawable.app_icon) // Equivalent of what ends up in onBitmapFailed
                .centerCrop()
                .fit()
                .into(holder.imgGal);



        holder.imgGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOpener(mValues.get(position).getImage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView imgGal;
        Button btnExit;
        public ArrayList<FeeModel> mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgGal = (ImageView) view.findViewById(R.id.imgGal);
            btnExit = (Button) view.findViewById(R.id.btnExit);
        }

        @Override
        public String toString() {

            return "";
        }
    }

    public void showOpener(String img) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_zoommable_view);

        dialog.findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        PhotoView image = dialog.findViewById(R.id.img_user_img);
        try {
            Glide.with(activity).applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                    .load(img).into((image));

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();
    }
}
