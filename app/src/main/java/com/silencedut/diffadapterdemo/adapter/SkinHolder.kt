package com.silencedut.diffadapterdemo.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.holder.BaseDiffViewHolder
import com.silencedut.diffadapterdemo.LegendViewModel
import com.silencedut.diffadapterdemo.R
import com.squareup.picasso.Picasso

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class SkinHolder(itemView: View, recyclerAdapter: DiffAdapter): BaseDiffViewHolder<SkinViewData>( itemView,  recyclerAdapter){
    private var legendIcon :ImageView?=null
    private var legendSkin1 :ImageView?=null
    private var legendSkin2 :ImageView?=null

    companion object {
        const val TAG = "SkinHolder"
    }

    override fun getItemViewId(): Int {
        return SkinViewData.VIEW_ID
    }

    init {
        legendIcon = itemView.findViewById(R.id.legendIcon_iv)
        legendSkin1 = itemView.findViewById(R.id.skins1_iv)
        legendSkin2 = itemView.findViewById(R.id.skins2_iv)
        itemView.setOnClickListener {
            getViewModel(LegendViewModel::class.java).updateSkinHolder(data.id)
        }
    }

    override fun updateItem(data: SkinViewData, position: Int) {
        Log.d(TAG,"updateItem $data")
        data.legendIcon?.let {
            Picasso.get().load(it).into(legendIcon)
        }
        data.legendSkin?.let {
            when(it.skins.size) {
                1 ->  Picasso.get().load(it.skins[0]).into(legendIcon)
                2 ,3,4->  {
                    Picasso.get().load(it.skins[0]).into(legendSkin1)
                    Picasso.get().load(it.skins[1]).into(legendSkin2)
                }
            }

        }

    }

}