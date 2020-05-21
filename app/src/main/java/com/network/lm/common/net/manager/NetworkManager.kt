package com.network.lm.common.net.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.NonNull
import com.network.lm.common.configuration.Constants.NET_WORK_CONNECTION_STATUS_AND_NET_TYPE_CODE
import com.network.lm.common.eventbus.NetWorkEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

/**
 * 网络监听
 * @author liming
 * @date 2020/05/14
 */
class NetworkManager private constructor() {
    companion object {
        val INSTANCE: NetworkManager by lazy {
            NetworkManager()
        }
    }

    private lateinit var mContext: Context
    private lateinit var networkCallbackImpl: NetworkCallbackImpl

    //回调接口
    private val callbacks: MutableList<BaseNetworkStatus> = mutableListOf()

    var connection: Boolean = false
    var netType: NetType = NetType.NONE

    /**
     * 初始化方法
     */
    @SuppressLint("MissingPermission")
    fun init(context: Context) {
        mContext = context
        if (isHigherThenLollipop()) {
            networkCallbackImpl = NetworkCallbackImpl(::observerIsConnection, ::observerNetWorkType)
            val request = NetworkRequest.Builder().build()
            val manager = INSTANCE.mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            manager.registerNetworkCallback(request, networkCallbackImpl)
            netType = if (isHigherThenM()) {
                try {
                    val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                    getNetType21(capabilities)
                }catch (e : Exception){
                    e.printStackTrace()
                    NetType.NONE
                }
            } else {
                getNetType(manager)
            }
        } else {
            if (!EventBus.getDefault().isRegistered(INSTANCE))
                EventBus.getDefault().register(INSTANCE)
            //开启服务
            mContext.startService(Intent(mContext, NetworkService::class.java))
        }
    }

    /**
     * 销毁方法
     */
    fun destory() {
        if (isHigherThenLollipop()) {
            if (::networkCallbackImpl.isInitialized) {
                val manager = INSTANCE.mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                manager.unregisterNetworkCallback(networkCallbackImpl)
            }
        } else {
            //关闭服务
            mContext.stopService(Intent(mContext, NetworkService::class.java))
            if (EventBus.getDefault().isRegistered(INSTANCE))
                EventBus.getDefault().unregister(INSTANCE)
        }
        //清楚所有回调Callback
        clearAllNetWorkStatusCallback()
    }

    /**
     * 判断版本
     */
    private fun isHigherThenLollipop() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    private fun isHigherThenM() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**
     * 注册监听器
     * @param callback 网络状态监听器
     */
    fun registerNetWorkStatusCallback(@NonNull callback: BaseNetworkStatus) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }

    /**
     * 取消监听器
     * @param callback 网络状态监听器
     */
    fun unregisterNetWorkStatusCallback(@NonNull callback: BaseNetworkStatus) {
        if (callbacks.contains(callback)) {
            callbacks.remove(callback)
        }
    }

    /**
     * 清空所有监听器
     */
    private fun clearAllNetWorkStatusCallback() {
        if (callbacks.isNotEmpty())
            callbacks.clear()
    }

    /**
     * 是否连接网络
     * @param conn 是否连接网络。true连接；false断开连接
     */
    private fun observerIsConnection(conn: Boolean) {
        connection = conn
        if (callbacks.isNotEmpty()) {
            for (callback: BaseNetworkStatus in callbacks) {
                if (callback !is NetworkTypeCallback) {
                    callback.isConnect(conn)
                }
            }
        }
    }

    /**
     * 判断网络类型
     * @param type 网络类型
     */
    private fun observerNetWorkType(type: NetType) {
        netType = type
        if (callbacks.isNotEmpty()) {
            for (callback: BaseNetworkStatus in callbacks) {
                if (callback !is NetworkConnectCallback) {
                    callback.getNetworkType(type)
                }
            }
        }
    }

    /**
     * 接收网络状态事件
     * @param event 网络状态事件对象
     */
    @Subscribe
    fun onEventMainThread(event: NetWorkEvent) {
        if (event.what == NET_WORK_CONNECTION_STATUS_AND_NET_TYPE_CODE) {
            val isAvailable = event.data.getBoolean("NET_WORK_IS_CONNECTION")
            observerIsConnection(isAvailable)
            if(isAvailable){
                val netType = event.obj as NetType
                observerNetWorkType(netType)
            }
        }
    }
}