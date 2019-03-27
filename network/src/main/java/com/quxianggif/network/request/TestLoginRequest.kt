package com.quxianggif.network.request

import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.TestLogin

class TestLoginRequest : Request() {

    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return Request.GET
    }

    override fun listen(callback: Callback?) {
        setListener(callback)
        inFlight(TestLogin::class.java)
    }

    companion object {
        private val URL = "http://192.168.1.128:8080/mmall/user/login.do?username=15920515287&password=111111"
    }
}