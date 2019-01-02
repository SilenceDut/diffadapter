package com.silencedut.diffadapterdemo;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.silencedut.diffadapter.DiffAdapter;
import com.silencedut.diffadapter.data.BaseMutableData;
import com.silencedut.diffadapter.utils.ModelProvider;
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
        ((DefaultItemAnimator) mRVTest.getItemAnimator()).setSupportsChangeAnimations(false);



        mDiffAdapter.addUpdateMediator(changedImageSource, new UpdateFunction<DataSource,ImageData>() {
            @Override
            public Object providerMatchFeature(@NonNull DataSource input) {

                return input.getUid();
            }

            @Override
            public ImageData applyChange(@NonNull DataSource input, @NonNull ImageData originalData) {
                 originalData.setSourceId(input.getResId());
                 return originalData;
            }
        });


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelProvider.getModel(MainActivity.this,DataChangeViewModel.class).getChangedTextSource()
                        .setValue(new DataSource(4,R.drawable.ic_launcher_foreground,"time:"+System.currentTimeMillis()));
                ModelProvider.getModel(MainActivity.this,DataChangeViewModel.class).getChangedTextSource()
                        .setValue(new DataSource(5,R.drawable.ic_launcher_foreground,"time:"+System.currentTimeMillis()));
                ModelProvider.getModel(MainActivity.this,DataChangeViewModel.class).getChangedTextSource()
                        .setValue(new DataSource(6,R.drawable.ic_launcher_foreground,"time:"+System.currentTimeMillis()));
            }
        });

        ModelProvider.getModel(this,DataChangeViewModel.class).addUpdateMediator(mDiffAdapter);

        final MutableLiveData<DataSource2> changedTextSource2 = new MutableLiveData<>();
        mDiffAdapter.addUpdateMediator(changedTextSource2, new UpdateFunction<DataSource2,TextData>() {
            @Override
            public Object providerMatchFeature(@NonNull DataSource2 input) {
                return input.getUid();
            }
            @Override
            public TextData applyChange(@NonNull DataSource2 input, @NonNull TextData originalData) {
                originalData.setBackgroundColor(input.getBackgroundColor());
                return new TextData(originalData.getUid(),originalData.getContent(),originalData.getBackgroundColor());
            }
        });

//        mRVTest.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("MainActivity","DataSource change");
//                mRVTest.postDelayed(this,1500);
//                changedImageSource.setValue(new DataSource(uid,R.drawable.ic_launcher_foreground,"xixi"));
//                uid ++;
//                if(uid > 7) {
//                    uid =0;
//                }
//            }
//        },1500);

//        mRVTest.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                changedTextSource.setValue(new DataSource(uid,R.drawable.ic_launcher_foreground,"新的内容:"+uid*2));
//                mRVTest.postDelayed(this,1000);
//                uid ++;
//                if(uid > 7) {
//                    uid =0;
//                }
//
//            }
//        },1000);
//
//        mRVTest.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                changedTextSource2.setValue(new DataSource2(uid,Color.WHITE,""));
//                mRVTest.postDelayed(this,1300);
//
//            }
//        },1300);
    }



    private void scheduleUpdate() {
        List<BaseMutableData> datas = new ArrayList<>();

        datas.add(new ImageData(0, R.mipmap.ic_launcher_round, "launcher"));
        datas.add(new ImageData(0, R.drawable.ic_launcher_background, "launcher1"));

        datas.add(new TextData(4,"4",Color.TRANSPARENT));
        datas.add(new ImageData(1, R.mipmap.ic_launcher_round, "launcher_round2"));
        datas.add(new ImageData(1, R.drawable.ic_launcher_background, "launcher_round5"));
        datas.add(new TextData(5,"5",Color.TRANSPARENT));
        datas.add(new ImageData(2, R.mipmap.ic_launcher_round, "launcher_round3"));
        datas.add(new TextData(6,"6",Color.TRANSPARENT));
        datas.add(new ImageData(3, R.mipmap.ic_launcher_round, "launcher_round4"));


        datas.add(new TextData(7,"7",Color.TRANSPARENT));
        mDiffAdapter.setData(datas);
        Log.d("MainActivity","scheduleUpdate");
    }


}
