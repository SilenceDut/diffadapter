package com.silencedut.diffadapterdemo

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.silencedut.core.Transfer
import com.silencedut.core.provider.legend.ILegendDateProvider
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.data.BaseMutableData
import com.silencedut.diffadapter.rvhelper.RvHelper
import com.silencedut.diffadapter.utils.DiffModelProvider
import com.silencedut.diffadapterdemo.adapter.LegendHolder
import com.silencedut.diffadapterdemo.adapter.LegendViewData
import com.silencedut.diffadapterdemo.adapter.SkinHolder
import com.silencedut.diffadapterdemo.adapter.SkinViewData
import com.silencedut.taskscheduler.TaskScheduler
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author SilenceDut
 * @date 2019/1/17
 */
private const val TAG = "LOLActivity"
private const val MAX_COUNT = 5000
class LOLActivity : AppCompatActivity(){
    private var mRVTest : RecyclerView? = null
    private var mTestHandler : Handler?=null
    private var testStarted = false
    private var testCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRVTest = this.findViewById(R.id.rv_test)

        val legendViewModel = DiffModelProvider.getModel(this, LegendViewModel::class.java)
        val diffAdapter = DiffAdapter(this)

        //配置支持的Holder类型
        diffAdapter.registerHolder(SkinHolder::class.java, SkinViewData.VIEW_ID)
        diffAdapter.registerHolder(LegendHolder::class.java, LegendViewData.VIEW_ID)


        val linearLayoutManager = LinearLayoutManagerWrapper(this)
        mRVTest!!.layoutManager = linearLayoutManager
        mRVTest!!.adapter = diffAdapter

        //监听变化，自动刷新
        legendViewModel.addUpdateMediator(diffAdapter)

        legendViewModel.legendsData.observeForever{

            if (it != null) {
                val list = mutableListOf<BaseMutableData<*>>()
                list.addAll(it)

                diffAdapter.datas = list
            }

        }

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
                Toast.makeText(this,"现在多少条："+diffAdapter.itemCount,Toast.LENGTH_SHORT).show()
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

        findViewById<View>(R.id.forcible_crash_test).setOnClickListener { _ ->

            if (!testStarted) {
                verify(diffAdapter)?.let {
                    testStarted = true
                    findViewById<TextView>(R.id.forcible_crash_test).text = "停止"
                    mTestHandler = Handler()

                    mTestHandler?.let {
                        it.post(object : Runnable {
                            override fun run() {
                                if(verifyCount()) {
                                    return
                                }
                                val oneLegend = Transfer.getImpl(ILegendDateProvider::class.java).fetchOneLegends()
                                diffAdapter.addData(legendViewModel.convertToAdapterData(oneLegend))
                                val time = (100 until 130L).random()
                                it.postDelayed(this, time)
                            }
                        })

                        it.post(object : Runnable {
                            override fun run() {
                                if(verifyCount()) {
                                    return
                                }
                                legendViewModel.fetchLegends()
                                val time = (50 until 200L).random()
                                it.postDelayed(this, time)
                            }
                        })

                        it.post(object : Runnable {
                            override fun run() {
                                if(verifyCount()) {
                                    return
                                }
                                if(diffAdapter.itemCount>0) {
                                    diffAdapter.deleteData(diffAdapter.datas[(0 until diffAdapter.itemCount).random()])

                                    val insertIndex = (0 until diffAdapter.itemCount).random()
                                    val insertSize = (0 until diffAdapter.itemCount).random()

                                    val newList = ArrayList(diffAdapter.datas.subList(0, insertSize))
                                    diffAdapter.insertData(insertIndex, newList)


                                }
                                val time = (60 until 150L).random()
                                it.postDelayed(this, time)
                            }
                        })

                        it.post(object :Runnable{
                            override fun run() {
                               // diffAdapter.clear()
                                val time = (30 until 450L).random()
                                it.postDelayed(this, time)
                            }
                        })

                        it.post(object : Runnable {
                            override fun run() {
                                if(verifyCount()) {
                                    return
                                }
                                if(diffAdapter.itemCount>0) {
                                    val deleteIndex = (0 until diffAdapter.itemCount).random()
                                    val deleteSize = (0 until diffAdapter.itemCount).random()
                                    diffAdapter.deleteData(deleteIndex, deleteSize)

                                }

                                val time = (100 until 300L).random()
                                it.postDelayed(this, time)
                            }
                        })

                        it.post(object : Runnable {
                            override fun run() {
                                if(verifyCount()) {
                                    return
                                }
                                mRVTest?.let {
                                    RvHelper.scrollToBottom(it,(0 until diffAdapter.itemCount).random())
                                }

                                val time = (300 until 500L).random()
                                it.postDelayed(this, time)
                            }
                        })
                    }
                }
            } else {
                endTest()
            }
        }



        TaskScheduler.runOnUIThread(this,object : Runnable{
            override fun run() {
                if(lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    Toast.makeText(this@LOLActivity,"点击Item 刷新当前Item",Toast.LENGTH_SHORT).show()
                }
                TaskScheduler.runOnUIThread(this@LOLActivity,this,10000)
            }
        },10000)

        //如果更新很频繁，关闭动画能极大提高UI性能
        RvHelper.closeDefaultAnimator(mRVTest!!)

    }

    private fun endTest() {
        mTestHandler?.removeCallbacksAndMessages(null)
        findViewById<TextView>(R.id.forcible_crash_test).text = "暴力崩溃测试"
        mTestHandler?.removeCallbacksAndMessages(null)
        testStarted = false
    }

    private fun verify( diffAdapter: DiffAdapter):Any? {
        if(diffAdapter.itemCount == 0) {
            Toast.makeText(this,"先获取数据",Toast.LENGTH_LONG).show()
            return null
        }
        
        return Any()
    }

    private fun verifyCount() :Boolean{
        testCount++
        if(testCount > MAX_COUNT) {
            findViewById<TextView>(R.id.forcible_crash_test).text = "暴力崩溃测试"
            endTest()
            testCount = 0
            return true
        }
        return false
    }

    private fun IntRange.random() : Int {
        try {
            return Random().nextInt((endInclusive + 1) - start) +  start
        }catch (e : Exception) {
            Log.d(TAG,"exception on random",e)
        }
        return 0
    }


}