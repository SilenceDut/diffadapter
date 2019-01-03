package com.silencedut.diffadapter.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 *
 * @author SilenceDut
 * @date 2018/12/19
 */
public class AsyncListUpdateDiffer<T> {
    private final ListUpdateCallback mUpdateCallback;
    final AsyncDifferConfig<T> mConfig;
    final Executor mMainThreadExecutor;
    private static final Executor sMainThreadExecutor = new AsyncListUpdateDiffer.MainThreadExecutor();
    @Nullable
    private List<T> mList;
    @NonNull
    private List<T> mReadOnlyList;
    int mMaxScheduledGeneration;
    private RecyclerView.Adapter mAttachedAdapter;

    public AsyncListUpdateDiffer(@NonNull RecyclerView.Adapter adapter, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this(adapter,(new AdapterListUpdateCallback(adapter)),(new AsyncDifferConfig.Builder(diffCallback)).build());
        this.mAttachedAdapter = adapter;
    }

    public AsyncListUpdateDiffer(@NonNull RecyclerView.Adapter adapter,@NonNull ListUpdateCallback listUpdateCallback, @NonNull AsyncDifferConfig<T> config) {
        this.mAttachedAdapter = adapter;
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = config;
        this.mMainThreadExecutor = sMainThreadExecutor;
    }

    @NonNull
    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    public void submitList(@Nullable final List<T> newList) {
        final int runGeneration = ++this.mMaxScheduledGeneration;
        if (newList != this.mList) {
            if (newList == null) {
                int countRemoved = this.mList.size();
                this.mList = null;
                this.mReadOnlyList = Collections.emptyList();
                this.mUpdateCallback.onRemoved(0, countRemoved);
            } else if (this.mList == null) {
                this.mList = newList;
                this.mReadOnlyList = Collections.unmodifiableList(newList);
                this.mUpdateCallback.onInserted(0, newList.size());
            } else {
                final List<T> oldList = this.mList;
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
                                if (oldItem != null && newItem != null) {
                                    return AsyncListUpdateDiffer.this.mConfig.getDiffCallback().areItemsTheSame(oldItem, newItem);
                                } else {
                                    return oldItem == null && newItem == null;
                                }
                            }

                            @Override
                            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                                T oldItem = oldList.get(oldItemPosition);
                                T newItem = newList.get(newItemPosition);
                                if (oldItem != null && newItem != null) {
                                    return AsyncListUpdateDiffer.this.mConfig.getDiffCallback().areContentsTheSame(oldItem, newItem);
                                } else if (oldItem == null && newItem == null) {
                                    return true;
                                } else {
                                    throw new AssertionError();
                                }
                            }

                            @Override
                            @Nullable
                            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                                T oldItem = oldList.get(oldItemPosition);
                                T newItem = newList.get(newItemPosition);
                                if (oldItem != null && newItem != null) {
                                    return AsyncListUpdateDiffer.this.mConfig.getDiffCallback().getChangePayload(oldItem, newItem);
                                } else {
                                    throw new AssertionError();
                                }
                            }
                        });
                        AsyncListUpdateDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (AsyncListUpdateDiffer.this.mMaxScheduledGeneration == runGeneration) {
                                    AsyncListUpdateDiffer.this.latchList(newList, result);
                                }

                            }
                        });
                    }
                });
            }
        }
    }

    private void latchList(@NonNull List<T> newList, @NonNull DiffUtil.DiffResult diffResult) {
        updateInnerList(newList);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
    }

    public void updateSingleItem(int changedPosition, Bundle payload) {

        if(payload.isEmpty()) {
            mAttachedAdapter.notifyItemChanged(changedPosition);
        }else {
            mAttachedAdapter.notifyItemChanged(changedPosition,payload);
        }

    }

    public void updateInnerList(@NonNull List<T> newList){
        ++this.mMaxScheduledGeneration;
        this.mList = newList;
        this.mReadOnlyList = Collections.unmodifiableList(newList);
    }


    private static class MainThreadExecutor implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        @Override
        public void execute(@NonNull Runnable command) {
            this.mHandler.post(command);
        }
    }


}
