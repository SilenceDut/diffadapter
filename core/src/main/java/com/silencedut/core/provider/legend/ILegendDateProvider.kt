package com.silencedut.core.provider.legend


import com.silencedut.core.provider.legend.pojo.Legend
import com.silencedut.core.provider.legend.pojo.LegendBaseInfo
import com.silencedut.core.provider.legend.pojo.LegendPrice
import com.silencedut.core.provider.legend.pojo.LegendSkin
import com.silencedut.hub.IHub

/**
 * @author SilenceDut
 * @date 2019/1/17
 */
interface ILegendDateProvider:IHub{
    /**
     * 获取列表
     */
    fun fetchLegends()

    /**
     * 获取一个
     */
    fun fetchOneLegends(): Legend

    fun legendsDataChanged()

    fun updateLegendSkin(legendId:Long)

    fun updateLegendNameAndPrice(legendId:Long)

    /**
     * 通过id获取数据，如果有缓存直接返回，否则返回空，同时模拟获取数据
     */
    fun baseLegendData(id:Long) : LegendBaseInfo?

    fun legendPrice(id:Long) : LegendPrice?

    fun legendSkin(id:Long) : LegendSkin?
}