package com.quyangyu.giffun.`interface`

interface  RequestLifecycle{
    fun startLoading()

    fun loadFinished()

    fun loadFailed(msg: String?)
}