package com.example.jsdkdemo

import android.app.Application
import cn.imeiadx.jsdk.util.OaidManager

/**
 * @author liuhuijie
 * @date 2020/11/1
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesUtils.init(this)
        OaidManager.getOaid(this)
    }
}