package com.ritara.svustudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList images;
    private ViewPager vp;
    private int mCurrentPosition = -1;
    private View view;

    public ViewPagerAdapter(Context context, ArrayList screens) {
        this.context = context;
        this.images = screens;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.custom_layout, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Picasso.get()
                .load(images.get(position).toString()).into(imageView);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof DynamicViewPager) {
            RelativeLayout fragment = (RelativeLayout) object;
            DynamicViewPager pager = (DynamicViewPager) container;

            if (fragment != null && fragment != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment);
            }
        }
    }
}
