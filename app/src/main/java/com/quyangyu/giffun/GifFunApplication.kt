package com.quyangyu.giffun

import android.app.Application
import com.quxianggif.core.GifFun

class  GifFunApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        GifFun.initialize(this)
    }
}