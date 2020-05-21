package com.network.lm.core

import android.app.Application
import com.network.lm.common.net.manager.NetworkManager

class MyApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkManager.INSTANCE.init(this)
    }
}