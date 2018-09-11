package com.silencedut.diffadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 大部分情况下是不需要再Adapter做过多的逻辑操作的，Adapter的目的就是用来组织Holder
 *
 * @author SilenceDut
 * @date 2018/9/6
 */
public class DiffAdapter extends RecyclerView.Adapter<BaseDiffViewHolder> {

    private static final String TAG = "BaseRecyclerAdapter";
    private SparseArray<Class<? extends BaseDiffViewHolder>> typeHolders = new SparseArray();
    private List<BaseImmutableData> mData = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    protected DiffAdapter.HolderClickListener mHolderClickListener;
    private AsyncListDiffer<BaseImmutableData> mDifferHelper;

    /**
     * 一般情况下没处理getChangePayload，默认在{@link #onBindViewHolder(BaseDiffViewHolder holder, int position)()}.更新数据
     * ，而不是payload，使用payload的方式大大增加代码的繁琐性，降低库的易用性，实际的payload对性能的影响很小，只不过在跟新UI的时候少更新几项，
     *  如果对数据刷新很敏感，实现{@link BaseDiffPayloadData#Object getPayload(T newData)}即可
     *
     */
    @SuppressWarnings("unchecked")
    public DiffAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        mDifferHelper = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<BaseImmutableData>() {
            @Override
            public boolean areItemsTheSame(BaseImmutableData oldItem, BaseImmutableData newItem) {

                return oldItem.areSameItem(newItem);
            }

            @Override
            public boolean areContentsTheSame(BaseImmutableData oldItem, BaseImmutableData newItem) {

                return oldItem.areUISame(newItem);
            }

            @Override
            public Object getChangePayload(BaseImmutableData oldItem, BaseImmutableData newItem) {
                if(oldItem instanceof BaseDiffPayloadData) {
                    return ((BaseDiffPayloadData) oldItem).getPayload(newItem);
                }
                return super.getChangePayload(oldItem, newItem);
            }
        });
    }


    public void registerHolder(Class<? extends BaseDiffViewHolder> viewHolder, int itemViewType) {
        typeHolders.put(itemViewType, viewHolder);
    }

    public <T extends BaseImmutableData> void registerHolder(Class<? extends BaseDiffViewHolder> viewHolder, T data) {
        if (data == null) {
            return;
        }
        typeHolders.put(data.getItemViewId(), viewHolder);
        addData(data);
    }

    public void registerHolder(Class<? extends BaseDiffViewHolder> viewHolder, List<? extends BaseImmutableData> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        typeHolders.put(data.get(0).getItemViewId(), viewHolder);
        setData(data);
    }

    /**
     * 默认列表里的对象都是新new出来的
     * @param datas 数据
     */
    public void setData(List<? extends BaseImmutableData> datas) {
        setData(datas,true);
    }

    /**
     * @param datas 需要展示的数据
     * @param isNew 每个数据是否是新创建的对象
     */
    public void setData(List<? extends BaseImmutableData> datas ,boolean isNew) {
        mData.clear();
        addData(datas,isNew);
    }

    public void clear() {
        mData.clear();
        doNotifyUI();
    }

    public <T extends BaseImmutableData> void addData(T data) {
        if (data == null) {
            return;
        }
        mData.add(data.copyData());
        doNotifyUI();
    }


    /**
     * 默认列表里的对象都是新new出来的
     * @param datas 数据
     */
    public <T extends BaseImmutableData> void addData(List<T> datas) {
        addData(datas,true);
    }

    /**
     *
     * @param datas 新增的需要展示的数据
     * @param isNew 每个数据是否是新创建的对象
     */
    public <T extends BaseImmutableData> void addData(List<T> datas,boolean isNew) {
        if (datas == null) {
            return;
        }
        if(isNew) {
            mData.addAll(datas);
        }else {
            for(BaseImmutableData data : datas) {
                mData.add( data.copyData());
            }
        }
        doNotifyUI();
    }

    public void updateData(BaseImmutableData oldData) {
        if (oldData == null ) {
            return;
        }
        Iterator<BaseImmutableData> iterator = mData.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            index ++;
            if(oldData.areSameItem(iterator.next())) {
                iterator.remove();
                break;
            }
        }

        if( index != -1 ) {
            if(index < mData.size()) {
                mData.add(index,oldData.copyData());
            }
            doNotifyUI();
        }

    }

    public void deleteData(BaseImmutableData data) {
        if (data == null) {
            return;
        }
        Iterator<BaseImmutableData> iterator = mData.iterator();
        while (iterator.hasNext()) {
            if(data.areSameItem(iterator.next())) {
                iterator.remove();
                break;
            }
        }
        doNotifyUI();
    }

    private void doNotifyUI() {
        List<BaseImmutableData> newList = new ArrayList<>(mData);
        mDifferHelper.submitList(newList);
    }

    @NonNull
    @Override
    public BaseDiffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(viewType, parent, false);
        BaseDiffViewHolder viewHolder = new NoDataDifferHolder(itemView, this);
        try {
            Class<?> cls = typeHolders.get(viewType);
            Constructor holderConstructor = cls.getDeclaredConstructor(View.class, DiffAdapter.class);
            holderConstructor.setAccessible(true);
            viewHolder = (BaseDiffViewHolder) holderConstructor.newInstance(itemView, this);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Create"+typeHolders.get(viewType)+" error,is it a static inner class? can't create no static inner ViewHolder ");
        } catch (Exception e) {
            Log.e(TAG, e.getCause() + "");
        }
        return viewHolder;
    }


    public <T extends BaseImmutableData> List<T> getData (Class<T> tClass) {
        List<T> typeLists = new ArrayList<>();
        for(BaseImmutableData baseImmutableData : mData) {
            if(tClass.isInstance(baseImmutableData)) {
                typeLists.add((T) baseImmutableData);
            }
        }
        return typeLists;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseDiffViewHolder holder, int position) {
        if (mDifferHelper.getCurrentList().size() == 0  || mDifferHelper.getCurrentList().get(position)==null) {
            return;
        }

        if (getItemViewType(position) != holder.getItemViewId()) {
            return;
        }

        try {
            holder.updateItem(mDifferHelper.getCurrentList().get(position), position);
        }catch (Exception e) {
            Log.e(TAG,"onBindViewHolder updateItem error",e);
        }

    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mDifferHelper.getCurrentList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDifferHelper.getCurrentList().get(position).getItemViewId();
    }

    public void setOnHolderClickListener(DiffAdapter.HolderClickListener clickListener) {
        this.mHolderClickListener = clickListener;
    }

    public  interface HolderClickListener <T extends BaseImmutableData>{
        void onHolderClicked(int position, T data);
    }

    public <T extends BaseImmutableData> void onHolderClicked(int position, T data) {
        if(mHolderClickListener!=null) {
            mHolderClickListener.onHolderClicked(position,data);
        }
    }
}
