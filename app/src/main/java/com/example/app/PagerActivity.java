package com.example.app;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class PagerActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_tab);

        LayoutInflater lf = getLayoutInflater().from(this);
        View view1 = lf.inflate(R.layout.activity_main,null);
        View view2 = lf.inflate(R.layout.activity_extract,null);

        List<View> viewList = new ArrayList<>();
        viewList.add(view1);
        viewList.add(view2);

        ViewPager vp = findViewById(R.id.vp);


        MyAdapter myAdapter = new MyAdapter(viewList);
        vp.setAdapter(myAdapter);
    }

}
