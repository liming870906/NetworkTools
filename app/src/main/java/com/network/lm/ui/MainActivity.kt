package com.network.lm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.network.lm.R
import com.network.lm.common.net.manager.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author liming
 * @date 2020/05/21
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //初始网络状态及类型
        setTVContentViewValue(NetworkManager.INSTANCE.connection)
        setTVNetTypeValue(NetworkManager.INSTANCE.netType)
    }

    val callback = object : BaseNetworkStatus() {
        override fun isConnect(available: Boolean) {
            runOnUiThread {
                setTVContentViewValue(available)
            }
        }

        override fun getNetworkType(type: NetType) {
            runOnUiThread {
                setTVNetTypeValue(type)
            }
        }

    }

    val callback1 = object : NetworkConnectCallback() {
        override fun isConnect(available: Boolean) {
            runOnUiThread {
                setTVContentViewValue(available)
            }
        }

    }

    val callback2 = object : NetworkTypeCallback() {
        override fun getNetworkType(type: NetType) {
            runOnUiThread {
                setTVNetTypeValue(type)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        NetworkManager.INSTANCE.registerNetWorkStatusCallback(callback)
    }

    override fun onStop() {
        super.onStop()
        NetworkManager.INSTANCE.unregisterNetWorkStatusCallback(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        NetworkManager.INSTANCE.destory()
    }

    private fun setTVNetTypeValue(type: NetType) {
        tv_net_type.text = "Network Type：${type.name}"
    }

    private fun setTVContentViewValue(available: Boolean) {
        tv_is_connection.text = if (available) "The network connection" else "Network disconnection"
    }

}
