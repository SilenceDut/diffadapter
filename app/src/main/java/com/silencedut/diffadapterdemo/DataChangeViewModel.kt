package com.silencedut.diffadapterdemo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.data.BaseMutableData
import com.silencedut.diffadapter.utils.UpdateFunction

/**
 * @author SilenceDut
 * @date 2018/12/29
 */
class DataChangeViewModel : ViewModel(){
    val changedTextSource = MutableLiveData<DataSource>()



    fun addUpdateMediator(diffAdapter: DiffAdapter) {
        diffAdapter.addUpdateMediator(changedTextSource,  object : UpdateFunction<DataSource, BaseMutableData<*>> {
            override fun providerMatchFeature(input: DataSource): Any {
                return input.uid
            }

            override fun applyChange(input: DataSource, originalData: BaseMutableData<*>): BaseMutableData<*> {
                when(originalData) {
                    is TextData ->  return TextData(originalData.uid,input.content,originalData.backgroundColor)
                }
                return originalData
            }
        })
    }
}