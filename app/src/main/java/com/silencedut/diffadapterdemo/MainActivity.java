package com.silencedut.diffadapterdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.silencedut.diffadapter.BaseMutableData;
import com.silencedut.diffadapter.DiffAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRVTest;
    private DiffAdapter mDiffAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRVTest = findViewById(R.id.rv_test);
        mDiffAdapter = new DiffAdapter(this);
        mDiffAdapter.registerHolder(ImageHolder.class,ImageData.VIEW_ID);
        mDiffAdapter.registerHolder(TextHolder.class,TextData.VIEW_ID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRVTest.setLayoutManager(linearLayoutManager);
        mRVTest.setAdapter(mDiffAdapter);
        mRVTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                scheduleUpdate();
                mRVTest.postDelayed(this,3000);

            }
        },3000);

        mRVTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                scheduleUpdate2();
                mRVTest.postDelayed(this,5000);

            }
        },5000);

    }

    private void scheduleUpdate() {
        List<BaseMutableData> datas = new ArrayList<>();
        datas.add(new ImageData("launcher",R.mipmap.ic_launcher));
        datas.add(new TextData(0,"0"));
        datas.add(new ImageData("launcher_round",R.mipmap.ic_launcher_round));
        datas.add(new TextData(1,"1"));
        datas.add(new ImageData("launcher_bg",R.drawable.ic_launcher_background));
        datas.add(new TextData(2,"2"));
        datas.add(new ImageData("launcher_fo",R.drawable.ic_launcher_foreground));
        datas.add(new TextData(3,"3"));
        mDiffAdapter.setData(datas);
        Log.d("MainActivity","scheduleUpdate");
    }

    private void scheduleUpdate2() {
        List<BaseMutableData> datas = new ArrayList<>();

        datas.add(new ImageData("launcher_round",R.mipmap.ic_launcher_round));
        datas.add(new TextData(0,"0"));
        datas.add(new ImageData("launcher_fo",R.drawable.ic_launcher_foreground));
        datas.add(new TextData(3,"3"));
        datas.add(new ImageData("launcher_bg",R.drawable.ic_launcher_background));
        datas.add(new TextData(2,"2"));
        datas.add(new TextData(1,"1"));
        datas.add(new ImageData("launcher",R.mipmap.ic_launcher));

        mDiffAdapter.setData(datas);
        Log.d("MainActivity","scheduleUpdate2");
    }
}
