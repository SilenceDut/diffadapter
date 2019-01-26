package com.silencedut.diffadapterdemo.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.holder.BaseDiffViewHolder
import com.silencedut.diffadapterdemo.LegendViewModel
import com.silencedut.diffadapterdemo.R
import com.squareup.picasso.Picasso

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class LegendHolder(itemViewRoot: View, recyclerAdapter: DiffAdapter): BaseDiffViewHolder<LegendViewData>( itemViewRoot,  recyclerAdapter){
    private var legendNameTv : TextView?=null
    private var legendIconIv : ImageView?=null
    private var legendPriceTv : TextView?=null
    companion object {
        const val TAG ="LegendHolder"
    }

    override fun getItemViewId(): Int {
        return LegendViewData.VIEW_ID
    }

    init {
        legendIconIv = itemViewRoot.findViewById(R.id.legendIcon_iv)
        legendNameTv = itemViewRoot.findViewById(R.id.legendName_tv)
        legendPriceTv = itemViewRoot.findViewById(R.id.legend_price)
        itemView.setOnClickListener {
            getViewModel(LegendViewModel::class.java).updateLegendHolder(data.id)
        }
    }

    override fun updateItem(data: LegendViewData, position: Int) {
        Log.d(TAG,"updateItem $data")
        updateBaseInfo(data)
        updatePrice(data)

    }

    private fun updateBaseInfo(data: LegendViewData) {
        data.legendBaseInfo?.let {
            legendNameTv?.text = it.name
            Picasso.get().load(it.iconUrl).into(legendIconIv)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePrice(data: LegendViewData) {
        data.price?.let {
            legendPriceTv?.text = "￥$it"
        }
    }

    /**
     * 最高效的更新方式，如果不是频繁更新的可以不实现这个方法
     */
    override fun updatePartWithPayload(data: LegendViewData, payload: Bundle, position: Int) {

        Log.d(TAG,"position :"+position+"updatePartWithPayload payload  :"+payload.getString(LegendViewData.KEY_BASE_INFO)+",price : "+payload.getString(LegendViewData.KEY_PRICE)+",data:"+data)
        if(payload.getString(LegendViewData.KEY_BASE_INFO)!=null) {
            updateBaseInfo(data)
        }
        if(payload.getString(LegendViewData.KEY_PRICE)!=null) {
            updatePrice(data)
        }
    }

}