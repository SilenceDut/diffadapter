package com.silencedut.diffadapterdemo.adapter

import android.os.Bundle
import android.util.Log
import com.silencedut.core.provider.legend.pojo.LegendBaseInfo
import com.silencedut.diffadapter.data.BaseMutableData
import com.silencedut.diffadapterdemo.R

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
data class LegendViewData(var id:Long, var legendBaseInfo: LegendBaseInfo?, var price: Long?) : BaseMutableData<LegendViewData>() {

    companion object {
        const val VIEW_ID = R.layout.holder_legend_introduce
        const val KEY_BASE_INFO = "KEY_BASE_INFO"
        const val KEY_PRICE = "KEY_PRICE"
    }


    override fun getItemViewId(): Int {
        return VIEW_ID
    }


    override fun areUISame(newData: LegendViewData): Boolean {
        return this.legendBaseInfo?.id == newData.legendBaseInfo?.id && this.price == newData.price
    }


    override fun appendPayloadKeys(newData: LegendViewData, payloadKeys: MutableSet<String>) {
        super.appendPayloadKeys(newData, payloadKeys)
        if(this.legendBaseInfo != newData.legendBaseInfo) {
            payloadKeys.add(KEY_BASE_INFO)
            Log.d("LegendViewData","appendDiffPayload"+KEY_BASE_INFO)
        }
        if(this.price != newData.price) {
            payloadKeys.add(KEY_PRICE)
            Log.d("LegendViewData","appendDiffPayload"+KEY_PRICE)
        }
    }

    override fun uniqueItemFeature(): Any {
       return this.id
    }

}