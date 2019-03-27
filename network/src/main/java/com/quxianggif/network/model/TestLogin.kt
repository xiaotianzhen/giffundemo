package com.quxianggif.network.model

import com.quxianggif.network.request.TestLoginRequest


class TestLogin:Response(){

    companion object {

        fun getResponse(callback: Callback) {
            TestLoginRequest().listen(callback)
        }
    }
}