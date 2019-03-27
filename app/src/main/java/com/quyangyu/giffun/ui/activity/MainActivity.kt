package com.quyangyu.giffun.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.quyangyu.giffun.R
import com.quyangyu.giffun.ui.adapter.TestAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var data:List<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data= listOf("瓦洛兰",
                "德玛西亚",
                "班德尔城",
                "诺克萨斯",
                "祖安",
                "瓦洛兰",
                "德玛西亚",
                "班德尔城",
                "诺克萨斯",
                "祖安")

        rv_test.adapter=TestAdapter(this,data!!)
        var layoutManager=LinearLayoutManager(this)
        rv_test.layoutManager=layoutManager;
    }


    companion object {
        private const val TAG = "MainActivity"
        
        fun actionStart(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

}
