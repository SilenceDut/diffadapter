package com.silencedut.diffadapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silencedut.diffadapter.data.BaseDiffPayloadData;
import com.silencedut.diffadapter.data.BaseMutableData;
import com.silencedut.diffadapter.holder.BaseDiffViewHolder;
import com.silencedut.diffadapter.holder.NoDataDifferHolder;
import com.silencedut.diffadapter.utils.AsyncListUpdateDiffer;
import com.silencedut.diffadapter.utils.UpdateFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 大部分情况下是不需要再Adapter做过多的逻辑操作的，Adapter的目的就是用来组织Holder
 *
 * @author SilenceDut
 * @date 2018/9/6
 *
 * When the async code is done, you should update the data, not the views. After updating the data, tell the adapter that the data changed. The RecyclerView gets note of this and re-renders your view.
 * When working with recycling views (ListView or RecyclerView), you cannot know what item a view is representing. In your case, that view gets recycled before the async work is done and is assigned to a different item of your data.
 * So never modify the view. Always modify the data and notify the adapter. bindView should be the place where you treat these cases.
 *
 * 异步数据结果回来不应该直接改变view的状态，而是应该改变数据，让数据驱动view改变
 */
public class DiffAdapter extends RecyclerView.Adapter<BaseDiffViewHolder> {

    private static final String TAG = "DiffAdapter";
    private SparseArray<Class<? extends BaseDiffViewHolder>> typeHolders = new SparseArray();
    private List<BaseMutableData> mData = new ArrayList<>();

    private LayoutInflater mInflater;
    private LifecycleOwner mLifecycleOwner;
    private AsyncListUpdateDiffer<BaseMutableData> mDifferHelper;
    private MediatorLiveData mUpdateMediatorLiveData = new MediatorLiveData<>();
    public DiffAdapter.HolderClickListener mHolderClickListener;
    public Fragment attachedFragment;
    public Context mContext;

    /**
     * 一般情况下没处理getChangePayload，默认在{@link #onBindViewHolder(BaseDiffViewHolder holder, int position)()}.更新数据
     * ，而不是payload，使用payload的方式大大增加代码的繁琐性，降低库的易用性，实际的payload对性能的影响很小，只不过在跟新UI的时候少更新几项，
     *  如果对数据刷新很敏感，实现{@link BaseDiffPayloadData#Object getPayload(T newData)}即可
     *
     */
    @SuppressWarnings("unchecked")
    public DiffAdapter(FragmentActivity appCompatActivity) {
        this.mContext = appCompatActivity;
        this.mInflater = LayoutInflater.from(appCompatActivity);
        this.mLifecycleOwner = appCompatActivity;
        this.mUpdateMediatorLiveData.observe(mLifecycleOwner, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {

            }
        });

        mDifferHelper = new AsyncListUpdateDiffer(this, new DiffUtil.ItemCallback<BaseMutableData>() {
            @Override
            public boolean areItemsTheSame(@NonNull BaseMutableData oldItem, @NonNull BaseMutableData newItem) {
                return oldItem.getItemViewId() == newItem.getItemViewId()
                        && oldItem.uniqueItemFeature().equals(newItem.uniqueItemFeature());

            }

            @Override
            public boolean areContentsTheSame(@NonNull BaseMutableData oldItem, @NonNull BaseMutableData newItem) {

                return oldItem.areUISame(newItem);
            }

            @Override
            public Object getChangePayload(@NonNull BaseMutableData oldItem, @NonNull BaseMutableData newItem) {
                if (oldItem instanceof BaseDiffPayloadData) {
                    return ((BaseDiffPayloadData) oldItem).getPayload(newItem);
                }
                return super.getChangePayload(oldItem, newItem);
            }
        });
    }

    public DiffAdapter(Fragment attachedFragment) {
        this(attachedFragment.getActivity());
        this.attachedFragment = attachedFragment;
        this.mLifecycleOwner = attachedFragment;
    }

    public void registerHolder(Class<? extends BaseDiffViewHolder> viewHolder, int itemViewType) {
        typeHolders.put(itemViewType, viewHolder);
    }

    public <T extends BaseMutableData> void registerHolder(Class<? extends BaseDiffViewHolder> viewHolder, T data) {
        if (data == null) {
            return;
        }
        typeHolders.put(data.getItemViewId(), viewHolder);

        addData(data);
    }

    public void registerHolder(Class<? extends BaseDiffViewHolder> viewHolder, List<? extends BaseMutableData> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        typeHolders.put(data.get(0).getItemViewId(), viewHolder);
        setData(data);
    }

    public <T> void  addUpdateMediator(LiveData<T> elementData, final UpdateFunction updateFunction) {
        mUpdateMediatorLiveData.addSource(elementData, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T dataSource) {

                List<BaseMutableData> oldMatchedDatas = getMatchedData(updateFunction.providerMatchFeature(dataSource));

                for(BaseMutableData oldData : oldMatchedDatas) {
                    ParameterizedType parameterizedType = (ParameterizedType) updateFunction.getClass().getGenericInterfaces()[0];
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                    if(oldData != null &&  actualTypeArguments.length > 1 && actualTypeArguments[1] == oldData.getClass()) {
                        updateData(updateFunction.applyChange(dataSource,oldData));
                    }
                }
            }
        });
    }


    /**
     * @param datas 需要展示的数据,如果数据的每一项不是新new出来的，需要自行实现copyData来创建新的对象
     */
    public void setData(List<? extends BaseMutableData> datas) {
        mData.clear();
        addData(datas);
    }

    public void clear() {
        mData.clear();
        doNotifyUI();
    }

    public <T extends BaseMutableData> void addData(T data) {
        if (data == null) {
            return;
        }
        mData.add(data);
        doNotifyUI();
    }


    /**
     * 默认列表里的对象都是新new出来的
     * @param datas 数据
     */
    public <T extends BaseMutableData> void addData(List<T> datas) {
        if (datas == null) {
            return;
        }
        mData.addAll(datas);
        doNotifyUI();
    }


    /**
     * newData must a new created object
     * @return isFound and Update
     */
    public boolean updateData(BaseMutableData newData) {
        if (newData == null ) {
            return false;
        }

        Iterator<BaseMutableData> iterator = mData.iterator();
        int foundIndex = -1;

        while (iterator.hasNext()) {
            BaseMutableData data = iterator.next();
            foundIndex ++;

            if(newData == data) {
                // same instance change content
                mDifferHelper.updateSingleItem(mData,foundIndex);
                return true;
            } else if(data.getItemViewId() == newData.getItemViewId()
                    && newData.uniqueItemFeature().equals(data.uniqueItemFeature()) && !newData.areUISame(data)) {
                // diff instance has same feature
                iterator.remove();
                mData.add(foundIndex,newData);
                mDifferHelper.updateSingleItem(mData,foundIndex);
                return true;
            }
        }

        return false;
    }

    public void deleteData(BaseMutableData data) {
        if (data == null) {
            return;
        }
        Iterator<BaseMutableData> iterator = mData.iterator();
        while (iterator.hasNext()) {
            if(data.uniqueItemFeature().equals(iterator.next().uniqueItemFeature())) {
                iterator.remove();
                break;
            }
        }
        doNotifyUI();
    }

    public void deleteData(int startPosition, int size) {
        if (startPosition > mData.size()) {
            return;
        }
        Iterator<BaseMutableData> iterator = mData.iterator();
        int deleteSize =0;
        while (iterator.hasNext() && deleteSize < size) {
            iterator.remove();
            deleteSize++;
        }
        doNotifyUI();
    }

    public void insertData(int startPosition ,List<? extends BaseMutableData> datas) {
        if (datas == null) {
            return;
        }
        mData.addAll(startPosition,datas);
        doNotifyUI();
    }

    public void doNotifyUI() {
        List<BaseMutableData> newList = new ArrayList<>(mData);
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
            Log.e(TAG, "Create  error,is it a inner class? can't create no static inner ViewHolder ");
        } catch (Exception e) {
            Log.e(TAG, e.getCause() + "");
        }
        return viewHolder;
    }


    public List<BaseMutableData> getMatchedData(Object matchChangeFeature) {
        List<BaseMutableData> matchedMutableData = new ArrayList<>();
        for(BaseMutableData baseMutableData : mData) {
           if(baseMutableData!=null && baseMutableData.matchChangeFeature().equals(matchChangeFeature) ) {
               matchedMutableData.add(baseMutableData);
           }
        }
        return matchedMutableData;

    }

    public <T extends BaseMutableData> List<T> getData(Class<T> tClass) {
        List<T> typeLists = new ArrayList<>();
        for(BaseMutableData baseMutableData : mData) {
            if(tClass.isInstance(baseMutableData)) {
                typeLists.add((T) baseMutableData);
            }
        }
        return typeLists;
    }

    public List<BaseMutableData> getData() {
        return mData;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseDiffViewHolder holder, int position) {
        if (mDifferHelper.getCurrentList().size() == 0 || mDifferHelper.getCurrentList().get(position)==null) {
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

    public  interface HolderClickListener <T extends BaseMutableData>{
        void onHolderClicked(int position, T data);
    }

    public <T extends BaseMutableData> void onHolderClicked(int position, T data) {
        if(mHolderClickListener!=null) {
            mHolderClickListener.onHolderClicked(position,data);
        }
    }
}
