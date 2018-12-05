package com.silencedut.diffadapter;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * 所有的ViewModel在此获取，便于方便管理
 *
 * @author SilenceDut
 * @date 2018/9/6
 */

public class ModelProvider {

    private static final String TAG = "ModelProvider";

    /**
     * 一个类中，对同一个  {@link ViewModel} 应该只调用一次getModel，然后类中做缓存
     * @param fragment ViewModelProvider 所依赖的fragment
     * @param viewModel 要获取的 BaseViewModel
     * @param <T> ViewModel 的实现
     * @return <T> 的实例
     */
    @MainThread
    public static <T extends ViewModel> T getModel(Fragment fragment , Class<T> viewModel) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()){
            throw new RuntimeException("getModel must call in mainThread");
        }

        return ModelProvider.getModel(fragment,viewModel);
    }

    /**
     *  see {@link #getModel}
     */
    @MainThread
    public static <T extends ViewModel> T getModel(Context context , Class<T> viewModel) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread() ){
            throw new RuntimeException("getModel must call in mainThread");
        }

        FragmentActivity fragmentActivity;

        if(context instanceof FragmentActivity) {
            fragmentActivity = (FragmentActivity)context;

        }else if(context instanceof ContextWrapper) {
            //某些情况下 4.x 及以下手机的context不是activity
            Log.i(TAG,"getModel from ContextWrapper BaseContext ");
            fragmentActivity = (FragmentActivity) ((ContextWrapper)context).getBaseContext();
        }else {
            throw new RuntimeException("context or ContextWrapper BaseContext must a FragmentActivity instance");
        }


        return ViewModelProviders.of(fragmentActivity).get(viewModel);
    }
}
