@file:Suppress("DEPRECATION")

package com.network.lm.common.net.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import com.network.lm.common.eventbus.NetWorkEvent
import com.network.lm.common.configuration.Constants.NET_WORK_CONNECTION_STATUS_AND_NET_TYPE_CODE
import com.network.lm.common.configuration.Constants.NET_WORK_IS_CONNECTION
import com.network.lm.common.eventbus.ConcreteFactory
import org.greenrobot.eventbus.EventBus

/**
 * 网络状态接收广播
 * @author liming
 * @date 2020/05/21
 */
class NetworkReceiver : BroadcastReceiver() {
    @Suppress("DEPRECATION")
    override fun onReceive(context: Context, intent: Intent?) {
        intent?.let {
            val action = it.action ?: "TT_EMPTY_RECEIVER_TAP"
            if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
                //判断网络类型
                val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netType = getNetType(manager)
                val isAvailable = netType != NetType.NONE
                val bundle = Bundle()
                bundle.putBoolean(NET_WORK_IS_CONNECTION,isAvailable)
                val event = ConcreteFactory().createEvent(NetWorkEvent::class.java)
                event.what = NET_WORK_CONNECTION_STATUS_AND_NET_TYPE_CODE
                event.obj = netType
                event.data = bundle
                EventBus.getDefault().post(event)
            }
        }
    }
}