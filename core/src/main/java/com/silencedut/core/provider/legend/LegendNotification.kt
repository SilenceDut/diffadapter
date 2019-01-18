package com.silencedut.core.provider.legend

import com.silencedut.core.provider.legend.pojo.LegendBaseInfo
import com.silencedut.core.provider.legend.pojo.LegendPrice
import com.silencedut.core.provider.legend.pojo.LegendSkin
import com.silencedut.core.provider.legend.pojo.Legend

/**
 * @author SilenceDut
 * @date 2019/1/18
 */
interface LegendNotification {

    interface LegendInfo {
        fun onLegendBaseInfoFetched(legendBaseInfo: LegendBaseInfo)
        fun onLegendPriceFetched(legendPrice: LegendPrice)
        fun onLegendSkinsFetched(legendSkin: LegendSkin)
    }


    interface LegendsList {
        fun onLegendsFetched(ids: List<Legend>)
    }
}