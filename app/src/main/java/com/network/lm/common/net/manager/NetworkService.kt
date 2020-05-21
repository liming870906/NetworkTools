package com.network.lm.common.net.manager

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.IBinder

class NetworkService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    private lateinit var networkReceiver: NetworkReceiver

    @Suppress("DEPRECATION")
    override fun onCreate() {
        super.onCreate()
        networkReceiver = NetworkReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }
}
