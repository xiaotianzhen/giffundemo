package com.quyangyu.giffun.`interface`

interface  PermissionListener{
    fun onGranted()

    fun onDenied(deniedPermissions: List<String>)
}