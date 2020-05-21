package com.network.lm.common.net.manager

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi


/**
 * 网络状态抽象类
 * @author liming
 * @date 2020/05/20
 */
abstract class BaseNetworkStatus {
    abstract fun isConnect(available: Boolean)

    abstract fun getNetworkType(type: NetType = NetType.NONE)
}

/**
 * 网络连接状态反馈类
 * @author liming
 * @date 2020/05/20
 */
abstract class NetworkConnectCallback : BaseNetworkStatus() {
    override fun getNetworkType(type: NetType) {}
}

/**
 * 网络类型回调方法
 * @author liming
 * @date 2020/05/20
 */
abstract class NetworkTypeCallback : BaseNetworkStatus() {
    override fun isConnect(available: Boolean) {}
}

/**
 * 网络类型
 * @author liming
 * @date 2020/05/20
 */
enum class NetType {
    //表示此网络使用蜂窝传输。
    CELLULAR,

    //表示此网络使用Wi-Fi传输。
    WIFI,

    //表示此网络使用蓝牙传输。
    BLUETOOTH,

    //表示此网络使用以太网传输。
    ETHERNET,

    //表示此网络使用VPN传输。
    VPN,

    //表示此网络使用可识别Wi-Fi的传输。
    WIFI_AWARE,

    //表示此网络使用LoWPAN传输。
    LOWPAN,

    //无网络状态
    NONE
}

/**
 * 获得网络类型
 * @param capabilities
 * @return NetType网络类型
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun getNetType21(capabilities: NetworkCapabilities): NetType = when {
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
        NetType.CELLULAR
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
        NetType.WIFI
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
        NetType.BLUETOOTH
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
        NetType.ETHERNET
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
        NetType.VPN
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> {
        NetType.WIFI_AWARE
    }
    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> {
        NetType.LOWPAN
    }
    else -> {
        NetType.NONE
    }
}

/**
 * 获得网络类型。建议：28版本一下使用
 * @param connectivityManager
 * @return NetType网络类型
 */
@Suppress("DEPRECATION")
fun getNetType(connectivityManager: ConnectivityManager): NetType {
    val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    return if (wifi != null && wifi.isAvailable && wifi.isConnectedOrConnecting) {
        NetType.WIFI
    } else if (mobile != null && mobile.isAvailable && mobile.isConnectedOrConnecting) {
        NetType.CELLULAR
    } else {
        NetType.NONE
    }
}