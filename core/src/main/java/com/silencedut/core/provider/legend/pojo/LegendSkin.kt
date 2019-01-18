package com.silencedut.core.provider.legend.pojo

import java.util.*

/**
 * @author SilenceDut
 * @date 2019/1/17
 */
data class LegendSkin(var id:Long, var skins: Array<String>){
    override fun equals(other: Any?): Boolean {

        return if(other is LegendSkin) {
            this.id == other.id && this.skins.contentDeepEquals(other.skins)

        }else {
            false
        }

    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + Arrays.hashCode(skins)
        return result
    }
}