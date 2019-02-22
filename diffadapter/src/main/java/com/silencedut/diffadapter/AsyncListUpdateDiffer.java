package com.silencedut.diffadapter;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.silencedut.diffadapter.data.BaseMutableData;
import com.silencedut.diffadapter.utils.ListChangedCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author SilenceDut
 * @date 2018/12/19
 */
class AsyncListUpdateDiffer<T extends BaseMutableData> {
    private static final String TAG ="AsyncListUpdateDiffer";
    private final ListUpdateCallback mUpdateCallback;
    private final AsyncDifferConfig<T> mConfig;
    private final ListChangedCallback<T> mListChangedCallback;
    @Nullable
    private List<T> mOldList;
    private long mMaxScheduledGeneration;
    private long mMaxSizeChangeGeneration;
    private long mCanSyncTime = 0;
    private Set<Long> mGenerations = new HashSet<>();
    static final int DELAY_STEP = 5;
    static final Handler DIFF_MAIN_HANDLER = new Handler(Looper.getMainLooper());


    AsyncListUpdateDiffer(@NonNull DiffAdapter adapter, @NonNull ListChangedCallback<T> listChangedCallback, @NonNull DiffUtil.ItemCallback<T> diffCallback) {

        this.mUpdateCallback = new AdapterListUpdateCallback(adapter);
        this.mConfig = new AsyncDifferConfig.Builder<>(diffCallback).build();
        this.mListChangedCallback = listChangedCallback;
        updateCurrentList(new ArrayList<T>());
    }

    private void updateCurrentList(List<T> currentList) {
        this.mListChangedCallback.onListChanged(currentList);
    }


    void submitList(@Nullable final List<T> newList) {
        final long runGeneration = ++this.mMaxScheduledGeneration;
        mGenerations.add(runGeneration);

        if (newList != this.mOldList) {
            if (newList == null) {
                int countRemoved = this.mOldList.size();
                syncGenerationAndList(null);
                updateCurrentList(new ArrayList<T>());
                this.mUpdateCallback.onRemoved(0, countRemoved);
                mGenerations.remove(runGeneration);
            } else if (this.mOldList == null) {
                syncGenerationAndList(newList);
                updateCurrentList(new ArrayList<>(newList));
                this.mUpdateCallback.onInserted(0, newList.size());
                mGenerations.remove(runGeneration);
            } else {
                doDiff(newList,runGeneration);
            }
        }
    }

    private void doDiff(@NonNull final List<T> newList,final long runGeneration) {
        final List<T> oldList = new ArrayList<>(this.mOldList);

        this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return oldList.size();
                    }
                    @Override
                    public int getNewListSize() {
                        return newList.size();
                    }
                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

                        T oldItem = oldList.get(oldItemPosition);
                        T newItem = newList.get(newItemPosition);
                        if(oldItem == null || newItem == null) {
                            return false;
                        }
                        if(oldItem.getItemViewId()!=newItem.getItemViewId() || oldItem.getClass() != newItem.getClass()) {
                            return false;
                        }
                        return AsyncListUpdateDiffer.this.mConfig.getDiffCallback().areItemsTheSame(oldItem, newItem);
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                        T oldItem = oldList.get(oldItemPosition);
                        T newItem = newList.get(newItemPosition);
                        if (oldItem != null && newItem != null &&  oldItem.getClass() == newItem.getClass() ) {
                            return AsyncListUpdateDiffer.this.mConfig.getDiffCallback().areContentsTheSame(oldItem, newItem);
                        } else  {
                            return oldItem == null && newItem == null;
                        }
                    }
                    @Override
                    @Nullable
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        T oldItem = oldList.get(oldItemPosition);
                        T newItem = newList.get(newItemPosition);
                        if (oldItem != null && newItem != null && oldItem.getClass() == newItem.getClass()) {
                            return AsyncListUpdateDiffer.this.mConfig.getDiffCallback().getChangePayload(oldItem, newItem);
                        } else {
                            return null;
                        }
                    }
                });
                DIFF_MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        if (AsyncListUpdateDiffer.this.mMaxScheduledGeneration == runGeneration) {
                            AsyncListUpdateDiffer.this.latchList(newList, result,runGeneration);
                        } else {
                            mGenerations.remove(runGeneration);
                        }
                    }
                });
            }
        });
    }

    private void latchList(@NonNull final List<T> newList, @NonNull final DiffUtil.DiffResult diffResult,final long runGeneration) {

        long needDelay = mCanSyncTime - SystemClock.elapsedRealtime() ;
        if(needDelay <= 0) {

            syncGenerationAndList(newList);
            updateCurrentList(new ArrayList<>(newList));
            diffResult.dispatchUpdatesTo(AsyncListUpdateDiffer.this.mUpdateCallback);
            mGenerations.remove(runGeneration);
        } else {
            final long runeGeneration = AsyncListUpdateDiffer.this.mMaxScheduledGeneration;
            final long sizeGeneration = AsyncListUpdateDiffer.this.mMaxSizeChangeGeneration;
            DIFF_MAIN_HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (AsyncListUpdateDiffer.this.mMaxScheduledGeneration == runeGeneration
                            && AsyncListUpdateDiffer.this.mMaxSizeChangeGeneration == sizeGeneration) {

                        syncGenerationAndList(newList);
                        updateCurrentList(new ArrayList<>(newList));
                        diffResult.dispatchUpdatesTo(AsyncListUpdateDiffer.this.mUpdateCallback);

                    }
                    mGenerations.remove(runGeneration);
                }
            }, needDelay );
        }

    }


    void updateOldListSize(final @NonNull Runnable listSizeRunnable , final List<T> oldDatas) {
        if(mGenerations.size() > 0) {
            return;
        }

        long currentTimeMillis = SystemClock.elapsedRealtime();

        if(currentTimeMillis >= mCanSyncTime) {

            listSizeRunnable.run();
            syncGenerationAndList(oldDatas);

        }else {
            final long sizeGeneration = AsyncListUpdateDiffer.this.mMaxSizeChangeGeneration;
            final long runGeneration =  AsyncListUpdateDiffer.this.mMaxScheduledGeneration;
            DIFF_MAIN_HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (AsyncListUpdateDiffer.this.mMaxSizeChangeGeneration == sizeGeneration
                            && runGeneration == AsyncListUpdateDiffer.this.mMaxScheduledGeneration) {

                        listSizeRunnable.run();
                        syncGenerationAndList(oldDatas);
                    }
                }
            }, mCanSyncTime - currentTimeMillis );
        }
    }

    private void syncGenerationAndList(@Nullable  List<T> oldData) {
        this.mOldList = oldData;
        mCanSyncTime = SystemClock.elapsedRealtime() + (oldData!=null?oldData.size() * DELAY_STEP:0) ;
        ++AsyncListUpdateDiffer.this.mMaxSizeChangeGeneration;
    }

}
