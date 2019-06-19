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


    /**
     * 最高效的更新方式，如果不是频繁更新的可以不实现这个方法
     */
    override fun appendDiffPayload(newData: LegendViewData, diffPayloadBundle: Bundle) {
        super.appendDiffPayload(newData, diffPayloadBundle)

        Log.d("LegendViewData","appendDiffPayload:"+newData)
        if(this.legendBaseInfo != newData.legendBaseInfo) {
            diffPayloadBundle.putString(KEY_BASE_INFO, KEY_BASE_INFO)
            Log.d("LegendViewData","appendDiffPayload"+KEY_BASE_INFO)
        }
        if(this.price != newData.price) {
            diffPayloadBundle.putString(KEY_PRICE, KEY_PRICE)
            Log.d("LegendViewData","appendDiffPayload"+KEY_PRICE)
        }

    }

    override fun uniqueItemFeature(): Any {
       return this.id
    }

}