package com.example.app;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAdapter extends PagerAdapter {


    private List<View> mlistview;

    public MyAdapter(List<View> listview){
        this.mlistview = listview;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewPager viewpager = (ViewPager) container;
        View view = mlistview.get(position);
        viewpager.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mlistview.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mlistview.get(position));
    }

}
