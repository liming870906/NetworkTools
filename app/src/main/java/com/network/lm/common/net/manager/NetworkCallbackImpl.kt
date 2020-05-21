package com.network.lm.common.net.manager

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 网络回馈实现类
 * @author liming
 * @date 2020/05/21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkCallbackImpl(val isConnection: (isconn: Boolean) -> Unit, val getNetType: (type: NetType) -> Unit) : ConnectivityManager.NetworkCallback() {
    /**
     * 网络连接方法
     * @param network
     */
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        isConnection(true)
    }

    /**
     * 网络断开方法
     * @param network
     */
    override fun onLost(network: Network) {
        super.onLost(network)
        isConnection(false)
    }

    /**
     * 检测网络类型
     * @param network
     * @param networkCapabilities
     */
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            getNetType(getNetType21(networkCapabilities))
        }
    }
}