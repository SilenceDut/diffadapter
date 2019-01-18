package com.silencedut.core

import com.silencedut.hub.Hub
import com.silencedut.hub.IHub
import com.silencedut.router.Router

/**
 * @author SilenceDut
 * @date 2019/1/18
 *
 * 统一管理跨模块通信框架，如果需要更换底层实现会很方便
 */
class Transfer {
    companion object {
        fun subscribe( objectAny : Any) {
            Router.instance().register(objectAny)
        }

        fun unSubscribe( objectAny : Any) {
            Router.instance().unregister(objectAny)
        }

        fun <T> getSubscriber(notificationCLs: Class<T>): T {
            return Router.instance().getReceiver(notificationCLs)
        }


        fun <T : IHub> getImpl(api: Class<T>): T {
            return Hub.getImpl(api)
        }

        /**
         * 除非特别确定后续不再需要这个实例里的状态，否则这个接口不要轻易调用
         *
         */
        fun <T : IHub> removeImpl(api: Class<T>) {
            Hub.removeImpl(api)
        }
    }
}