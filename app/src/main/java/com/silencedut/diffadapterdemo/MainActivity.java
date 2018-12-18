package com.silencedut.diffadapterdemo;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.silencedut.diffadapter.DiffAdapter;
import com.silencedut.diffadapter.data.BaseMutableData;
import com.silencedut.diffadapter.utils.UpdateFunction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRVTest;
    private DiffAdapter mDiffAdapter;

    private int uid =0;
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
        scheduleUpdate();

        final MutableLiveData<DataSource> changedImageSource = new MutableLiveData<>();

        mDiffAdapter.addUpdateMediator(changedImageSource, new UpdateFunction<DataSource,ImageData>() {
            @Override
            public Object providerUniqueFeature(DataSource input) {
                return input.getUid();
            }

            @Override
            public ImageData apply(DataSource input, ImageData originalData) {

                 return new ImageData(originalData.getUid(),input.getResId(),originalData.getName());
            }
        });

        final MutableLiveData<DataSource> changedTextSource = new MutableLiveData<>();

        mDiffAdapter.addUpdateMediator(changedTextSource, new UpdateFunction<DataSource,TextData>() {
            @Override
            public Object providerUniqueFeature(DataSource input) {
                return input.getUid();
            }

            @Override
            public TextData apply(DataSource input, TextData originalData) {

                return new TextData(originalData.getUid(),input.getContent(),originalData.getBackgroundColor());
            }
        });

        final MutableLiveData<DataSource2> changedTextSource2 = new MutableLiveData<>();
        mDiffAdapter.addUpdateMediator(changedTextSource2, new UpdateFunction<DataSource2,TextData>() {
            @Override
            public Object providerUniqueFeature(DataSource2 input) {
                return input.getUid();
            }

            @Override
            public TextData apply(DataSource2 input, TextData originalData) {

                return new TextData(originalData.getUid(),originalData.getContent(),input.getBackgroundColor());
            }
        });

        mRVTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity","DataSource change");
                mRVTest.postDelayed(this,1500);
                changedImageSource.setValue(new DataSource(uid,R.drawable.ic_launcher_foreground,"xixi"));
                uid ++;
                if(uid > 7) {
                    uid =0;
                }
            }
        },1500);

        mRVTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                changedTextSource.setValue(new DataSource(uid,R.drawable.ic_launcher_foreground,"新的内容:"+uid*2));
                mRVTest.postDelayed(this,1000);

            }
        },1000);

        mRVTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                changedTextSource2.setValue(new DataSource2(uid,Color.WHITE,""));
                mRVTest.postDelayed(this,1300);

            }
        },1300);
    }



    private void scheduleUpdate() {
        List<BaseMutableData> datas = new ArrayList<>();
        datas.add(new ImageData(0, R.mipmap.ic_launcher_round, "launcher"));

        datas.add(new TextData(4,"4",Color.TRANSPARENT));
        datas.add(new ImageData(1, R.mipmap.ic_launcher_round, "launcher_round"));
        datas.add(new TextData(5,"5",Color.TRANSPARENT));
        datas.add(new ImageData(2, R.mipmap.ic_launcher_round, "launcher_round"));
        datas.add(new TextData(6,"6",Color.TRANSPARENT));
        datas.add(new ImageData(3, R.mipmap.ic_launcher_round, "launcher_round"));


        datas.add(new TextData(7,"7",Color.TRANSPARENT));
        mDiffAdapter.setData(datas);
        Log.d("MainActivity","scheduleUpdate");
    }


}
