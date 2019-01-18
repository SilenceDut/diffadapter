package com.silencedut.diffadapterdemo

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.silencedut.core.Transfer
import com.silencedut.core.provider.legend.ILegendDateProvider
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.rvhelper.RvHelper
import com.silencedut.diffadapter.utils.DiffModelProvider
import com.silencedut.diffadapterdemo.adapter.LegendHolder
import com.silencedut.diffadapterdemo.adapter.LegendViewData
import com.silencedut.diffadapterdemo.adapter.SkinHolder
import com.silencedut.diffadapterdemo.adapter.SkinViewData
import java.util.*

/**
 * @author SilenceDut
 * @date 2019/1/17
 */
private const val TAG = "LOLActivity"
class LOLActivity : AppCompatActivity(){
    private var mRVTest : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRVTest = this.findViewById(R.id.rv_test)

        val legendViewModel = DiffModelProvider.getModel(this, LegendViewModel::class.java)
        val diffAdapter = DiffAdapter(this)

        //配置支持的Holder类型
        diffAdapter.registerHolder(SkinHolder::class.java, SkinViewData.VIEW_ID)
        diffAdapter.registerHolder(LegendHolder::class.java, LegendViewData.VIEW_ID)


        val linearLayoutManager = LinearLayoutManager(this)
        mRVTest!!.layoutManager = linearLayoutManager
        mRVTest!!.adapter = diffAdapter

        //监听变化，自动刷新
        legendViewModel.addUpdateMediator(diffAdapter)

        legendViewModel.legendsData.observe(this, Observer {
            diffAdapter.datas = it
        })

        findViewById<View>(R.id.fetch_data).setOnClickListener {
            legendViewModel.fetchLegends()
        }

        findViewById<View>(R.id.random_update).setOnClickListener { _ ->
            verify(diffAdapter)?.let {
                legendViewModel.legendsDataChanged()
            }

        }

        findViewById<View>(R.id.random_add).setOnClickListener { _ ->
            val oneLegend = Transfer.getImpl(ILegendDateProvider::class.java).fetchOneLegends()
            diffAdapter.addData(legendViewModel.convertToAdapterData(oneLegend))

        }

        findViewById<View>(R.id.random_insert).setOnClickListener { _ ->
            verify(diffAdapter)?.let {
                val insertIndex = (0 until diffAdapter.itemCount).random()
                val insertSize= (0 until diffAdapter.itemCount).random()

                val newList = ArrayList(diffAdapter.datas.subList(0,insertSize))
                diffAdapter.insertData(insertIndex,newList)
            }

        }

        findViewById<View>(R.id.random_delete_many).setOnClickListener{ _ ->
            verify(diffAdapter)?.let {
                val deleteIndex = (0 until diffAdapter.itemCount).random()
                val deleteSize= (0 until diffAdapter.itemCount).random()
                diffAdapter.deleteData(deleteIndex,deleteSize)
            }

        }

        findViewById<View>(R.id.random_delete_one).setOnClickListener { _ ->
            verify(diffAdapter)?.let {
                diffAdapter.deleteData(diffAdapter.datas[(0 until diffAdapter.itemCount).random()])
            }
           
        }



        //如果更新很频繁，关闭动画能极大提高UI性能
        RvHelper.closeDefaultAnimator(mRVTest!!)

    }

    private fun verify( diffAdapter: DiffAdapter):Any? {
        if(diffAdapter.itemCount == 0) {
            Toast.makeText(this,"先获取数据",Toast.LENGTH_LONG).show()
            return null
        }
        
        return Any()
    }

    private fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

}