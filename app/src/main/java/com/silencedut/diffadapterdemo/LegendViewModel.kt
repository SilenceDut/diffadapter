package com.silencedut.diffadapterdemo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.silencedut.core.Transfer
import com.silencedut.core.provider.legend.ILegendDateProvider
import com.silencedut.core.provider.legend.LegendNotification
import com.silencedut.core.provider.legend.pojo.*
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.data.BaseMutableData
import com.silencedut.diffadapter.utils.UpdateFunction
import com.silencedut.diffadapterdemo.adapter.LegendViewData
import com.silencedut.diffadapterdemo.adapter.SkinViewData

/**
 * @author SilenceDut
 * @date 2019/1/17
 */
class LegendViewModel: ViewModel(), LegendNotification.LegendInfo, LegendNotification.LegendsList{

    val legendsData = MutableLiveData<List<BaseMutableData<*>>>()
    private val legendBaseInfoData = MutableLiveData<LegendBaseInfo>()
    private val legendPriceData = MutableLiveData<LegendPrice>()
    private val legendSkinData = MutableLiveData<LegendSkin>()

    companion object {
        const val TAG = "LegendViewModel"
    }

    init {

        Transfer.subscribe(this)
    }

    override fun onCleared() {
        super.onCleared()
        Transfer.unSubscribe(this)
    }

    fun fetchLegends() {
        Transfer.getImpl(ILegendDateProvider::class.java).fetchLegends()
    }

    fun legendsDataChanged() {
        Transfer.getImpl(ILegendDateProvider::class.java).legendsDataChanged()
    }

    override fun onLegendsFetched(ids: List<Legend>) {
        legendsData.value = ids.map {
            convertToAdapterData(it)
        }
    }

    fun convertToAdapterData(legend: Legend) : BaseMutableData<*> {
        return when(legend.type) {
            Type.LEGEND -> LegendViewData(legend.id, Transfer.getImpl(ILegendDateProvider::class.java).baseLegendData(legend.id)
                    , Transfer.getImpl(ILegendDateProvider::class.java).legendPrice(legend.id)?.price)
            Type.SKIN -> SkinViewData(legend.id, Transfer.getImpl(ILegendDateProvider::class.java).baseLegendData(legend.id)?.iconUrl
                    , Transfer.getImpl(ILegendDateProvider::class.java).legendSkin(legend.id))
        }
    }


    fun addUpdateMediator(diffAdapter: DiffAdapter) {
        //如果变化的数据可能引起多种类型的holder的刷新，UpdateFunction的类型传入基础BaseMutableData<*就行，在applyChange在根据类型进行改变
        diffAdapter.addUpdateMediator(legendBaseInfoData,  object : UpdateFunction<LegendBaseInfo, BaseMutableData<*>> {
            override fun providerMatchFeature(input: LegendBaseInfo): Any {
                return input.id
            }

            override fun applyChange(input: LegendBaseInfo, originalData: BaseMutableData<*>): BaseMutableData<*> {
                Log.d(TAG,"applyChange $input")
                return when(originalData) {
                    is LegendViewData ->   LegendViewData(originalData.id, input, originalData.price)
                    is SkinViewData ->  SkinViewData(originalData.id, input.iconUrl, originalData.legendSkin)
                    else -> {
                        originalData
                    }
                }
            }
        })

        //如果变化的数据只需要特定类型的Holder刷新，类型即可指定
        diffAdapter.addUpdateMediator(legendPriceData,  object : UpdateFunction<LegendPrice, LegendViewData> {
            override fun providerMatchFeature(input: LegendPrice): Any {
                return input.id
            }

            override fun applyChange(input: LegendPrice, originalData: LegendViewData): LegendViewData {
                Log.d(TAG,"applyChange legendPriceData $input")
                //可以new对象
                return LegendViewData(originalData.id, originalData.legendBaseInfo, input.price)
            }
        })

        //如果变化的数据只需要特定类型的Holder刷新，类型即可指定
        diffAdapter.addUpdateMediator(legendSkinData,  object : UpdateFunction<LegendSkin, SkinViewData> {
            override fun providerMatchFeature(input: LegendSkin): Any {
                return input.id
            }

            override fun applyChange(input: LegendSkin, originalData: SkinViewData): SkinViewData {
                 Log.d(TAG,"applyChange legendSkinData $input")
                 //可以在原对象上修改
                 originalData.legendSkin = input
                 return originalData
            }
        })
    }

    override fun onLegendBaseInfoFetched(legendBaseInfo: LegendBaseInfo) {
        Log.d(TAG,"onLegendBaseInfoFetched $legendBaseInfo")
        legendBaseInfoData.value = legendBaseInfo
    }

    override fun onLegendPriceFetched(legendPrice: LegendPrice) {
        Log.d(TAG,"onLegendPriceFetched $legendPrice")
        legendPriceData.value = legendPrice

    }

    override fun onLegendSkinsFetched(legendSkin: LegendSkin) {
        Log.d(TAG,"onLegendSkinsFetched $legendSkin")
        legendSkinData.value = legendSkin
    }
}