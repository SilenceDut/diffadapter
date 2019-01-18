package com.silencedut.legend.impl

import android.util.Log
import com.silencedut.core.DataObject2
import com.silencedut.core.DataObject3
import com.silencedut.core.Transfer
import com.silencedut.core.provider.legend.ILegendDateProvider
import com.silencedut.core.provider.legend.LegendNotification
import com.silencedut.core.provider.legend.pojo.*
import com.silencedut.hub_annotation.HubInject
import com.silencedut.taskscheduler.TaskScheduler
import java.util.*
import kotlin.collections.HashMap

/**
 * @author SilenceDut
 * @date 2019/1/18
 */
@HubInject(api = [ILegendDateProvider::class])
class LegendDataProviderImpl : ILegendDateProvider{

    companion object {
        const val TAG = "LegendDataProviderImpl"
    }

    private val mLegendIds = arrayListOf(1L,2,3,4,5,6,7,8,9,10)

    private val mLegendsBaseInfoById = HashMap<Long,LegendBaseInfo>()
    private val mLegendsPriceById = HashMap<Long,LegendPrice>()
    private val mLegendsSkinsById = HashMap<Long,LegendSkin>()

    private val mBaseInfoServer = arrayListOf(DataObject3(1L,"暗裔剑魔 亚托克斯","https://ossweb-img.qq.com/images/lol/web201310/skin/big266000.jpg")
            ,DataObject3(2L,"九尾妖狐 阿狸","https://ossweb-img.qq.com/images/lol/web201310/skin/big103000.jpg")
            ,DataObject3(3L,"离群之刺 阿卡丽","https://ossweb-img.qq.com/images/lol/web201310/skin/big84000.jpg")
            ,DataObject3(4L,"诺克萨斯之手 德莱厄斯","https://ossweb-img.qq.com/images/lol/web201310/skin/big122000.jpg")
            ,DataObject3(5L,"海洋之灾 普朗克","https://ossweb-img.qq.com/images/lol/web201310/skin/big41000.jpg")
            ,DataObject3(6L,"武器大师 贾克斯","https://ossweb-img.qq.com/images/lol/web201310/skin/big24000.jpg")
            ,DataObject3(7L,"青钢影 卡蜜尔","https://ossweb-img.qq.com/images/lol/web201310/skin/big164000.jpg")
            ,DataObject3(8L,"德玛西亚之力 盖伦","https://ossweb-img.qq.com/images/lol/web201310/skin/big86000.jpg")
            ,DataObject3(9L,"暴走萝莉 金克丝","https://ossweb-img.qq.com/images/lol/web201310/skin/big222000.jpg")
            ,DataObject3(10L,"戏命师 烬","https://ossweb-img.qq.com/images/lol/web201310/skin/big202000.jpg"))
    private val mSkinServer = arrayListOf(DataObject2(1L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big266008.jpg", "https://ossweb-img.qq.com/images/lol/web201310/skin/big266003.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big266001.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big266002.jpg"))
            ,DataObject2(2L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big103015.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big103014.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big103007.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big103005.jpg"))
            ,DataObject2(3L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big84009.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big84005.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big84003.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big84004.jpg"))
            ,DataObject2(4L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big122015.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big122008.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big122001.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big122004.jpg"))
            ,DataObject2(5L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big41003.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big41008.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big41005.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big41002.jpg"))
            ,DataObject2(6L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big24013.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big24007.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big24008.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big24012.jpg"))
            ,DataObject2(7L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big164001.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big164002.jpg"))
            ,DataObject2(8L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big86013.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big86005.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big86002.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big86011.jpg"))
            ,DataObject2(9L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big222004.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big222002.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big222012.jpg"))
            ,DataObject2(10L, arrayOf("https://ossweb-img.qq.com/images/lol/web201310/skin/big202002.jpg","https://ossweb-img.qq.com/images/lol/web201310/skin/big202004.jpg"
            ,"https://ossweb-img.qq.com/images/lol/web201310/skin/big202001.jpg")))

    //模拟从服务器异步获取数据
    private val mServerHandler = TaskScheduler.provideHandler("LegendServer")

    override fun onCreate() {

    }

    override fun fetchLegends() {

        val startIndex = (0 until mLegendIds.size-5).random()
        val resultIds = mLegendIds.subList(startIndex,(startIndex+5 until mLegendIds.size+1).random())
        Log.d(TAG,"fetchLegends $resultIds")
        Transfer.getSubscriber(LegendNotification.LegendsList::class.java).onLegendsFetched(resultIds.map {
            Legend(it,Type.values()[(0 until 2).random()])
        })
    }

    override fun fetchOneLegends(): Legend{
        return Legend((1 until mLegendIds.size+1).random().toLong(),Type.values()[(0 until 2).random()])
    }

    override fun legendsDataChanged() {
        val startIndex = (0 until mLegendIds.size-1).random()
        val resultIds = mLegendIds.subList(startIndex,(startIndex+1 until mLegendIds.size+1).random())
        resultIds.forEach {
            mLegendsBaseInfoById[it]?.let { _info ->
                _info.name = _info.name.reversed()
                Transfer.getSubscriber(LegendNotification.LegendInfo::class.java).onLegendBaseInfoFetched(_info)
            }
            val legendPrice = LegendPrice(it,(30 until 100).random().toLong())
            Transfer.getSubscriber(LegendNotification.LegendInfo::class.java).onLegendPriceFetched(legendPrice)

            mLegendsSkinsById[it]?.let {_skins ->

                _skins.skins = _skins.skins.reversedArray()
                Transfer.getSubscriber(LegendNotification.LegendInfo::class.java).onLegendSkinsFetched(_skins)
            }

        }
    }



    override fun baseLegendData(id: Long): LegendBaseInfo? {
        if(!mLegendsBaseInfoById.containsKey(id)) {
            mServerHandler.postDelayed({
                mBaseInfoServer.forEach {
                    if(it.field1 == id) {
                        val legendBaseInfo = LegendBaseInfo(it.field1,it.field2,it.field3)
                        mLegendsBaseInfoById[id] = legendBaseInfo
                        Transfer.getSubscriber(LegendNotification.LegendInfo::class.java).onLegendBaseInfoFetched(legendBaseInfo)
                    }
                }
            },500L)
        }
        return mLegendsBaseInfoById[id]

    }

    override fun legendPrice(id: Long): LegendPrice? {
        if(!mLegendsPriceById.containsKey(id)) {
            mServerHandler.postDelayed({
                val legendPrice = LegendPrice(id,(30 until 100).random().toLong())
                mLegendsPriceById[id] = legendPrice
                Transfer.getSubscriber(LegendNotification.LegendInfo::class.java).onLegendPriceFetched(legendPrice)
            },500L)
        }
        return mLegendsPriceById[id]
    }

    override fun legendSkin(id: Long): LegendSkin? {
        if(!mLegendsSkinsById.containsKey(id)) {
            mServerHandler.postDelayed({
                mSkinServer.forEach {
                    if(it.field1 == id) {
                        val legendSkin = LegendSkin(it.field1,it.field2)
                        mLegendsSkinsById[id] = legendSkin
                        Transfer.getSubscriber(LegendNotification.LegendInfo::class.java).onLegendSkinsFetched(legendSkin)
                    }
                }
            },(100 until 500).random().toLong())
        }
        return mLegendsSkinsById[id]
    }

    private fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

}