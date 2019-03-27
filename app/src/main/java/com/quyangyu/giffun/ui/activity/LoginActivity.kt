package com.quyangyu.giffun.ui.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.transition.Transition
import android.view.View
import android.widget.Toast
import com.quxianggif.core.GifFun
import com.quxianggif.core.extension.logWarn
import com.quxianggif.core.extension.showToast
import com.quxianggif.core.model.Version
import com.quxianggif.core.util.AndroidVersion
import com.quxianggif.core.util.GlobalUtil
import com.quxianggif.network.model.Callback
import com.quxianggif.network.model.FetchVCode
import com.quxianggif.network.model.Response
import com.quxianggif.network.model.TestLogin
import com.quxianggif.network.request.TestLoginRequest
import com.quyangyu.giffun.R
import com.quyangyu.giffun.callback.SimpleTransitionListener
import com.quyangyu.giffun.event.FinishActivityEvent
import com.quyangyu.giffun.util.ResponseHandler
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {

    private lateinit var timer: CountDownTimer

    /**
     * 是否正在登录中。
     */
    private var isLogin = false

    /**
     * 是否正在进行transition动画。
     */
    protected var isTransitioning = false

    private var loginActivity:LoginActivity?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginActivity=this
    }


    override fun setupViews() {
        super.setupViews()
        val  isStartWithTransition=intent.getBooleanExtra(START_WITH_TRANSITION,false)
        if(AndroidVersion.hasLollipop()&&isStartWithTransition){
            isTransitioning=true
            window.sharedElementEnterTransition.addListener(object : SimpleTransitionListener(){
                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    val  event=FinishActivityEvent()
                    event.activityClass=SplashActivity::class.java
                    EventBus.getDefault().post(event)
                    isTransitioning=false
                    fadeElementsIn()
                }
            })
        }else{
            loginLayoutBottom.visibility = View.VISIBLE
            loginBgWallLayout.visibility = View.VISIBLE
        }

        timer=SMSTimer(60*1000,1000)
        getVerifyCode.setOnClickListener {
            val number=phoneNumberEdit.text.toString()
            if(number.isEmpty()){
                showToast("手机号不能为空")
                return@setOnClickListener
            }
            val pattern = "^1\\d{10}\$"
            if (!Pattern.matches(pattern, number)) {
                showToast(GlobalUtil.getString(R.string.phone_number_is_invalid))
                return@setOnClickListener
            }
            getVerifyCode.isClickable=false
            FetchVCode.getResponse(number, object : Callback {
                override fun onResponse(response: Response) {
                    if (response.status == 0) {
                        timer.start()
                        verifyCodeEdit.requestFocus()
                    } else {
                        showToast(response.msg)
                        getVerifyCode.isClickable = true
                    }
                }

                override fun onFailure(e: Exception) {
                    logWarn(TAG, e.message, e)
                    ResponseHandler.handleFailure(e)
                    getVerifyCode.isClickable = true
                }
            })
        }

        loginButton.setOnClickListener {
            val number=phoneNumberEdit.text.toString()
            if(number.isEmpty()){
                showToast("手机号不能为空")
                return@setOnClickListener
            }
            val pattern = "^1\\d{10}\$"
            if (!Pattern.matches(pattern, number)) {
                showToast(GlobalUtil.getString(R.string.phone_number_is_invalid))
                return@setOnClickListener
            }
            val code=verifyCodeEdit.text.toString()
            if(code.isEmpty()){
                showToast("验证码不能为空！")
                return@setOnClickListener
            }
            TestLogin.getResponse(object : Callback {
                override fun onResponse(response: Response) {
                    if (response.status == 0) {
                        MainActivity.actionStart(loginActivity!!)
                    } else {

                    }
                }

                override fun onFailure(e: Exception) {
                    logWarn(TAG, e.message, e)
                    MainActivity.actionStart(loginActivity!!)
                    ResponseHandler.handleFailure(e)
                    showToast("登录失败")
                }
            })



        }



    }

    private fun fadeElementsIn() {
        TransitionManager.beginDelayedTransition(loginLayoutBottom, Fade())
        loginLayoutBottom.visibility = View.VISIBLE
        TransitionManager.beginDelayedTransition(loginBgWallLayout, Fade())
        loginBgWallLayout.visibility = View.VISIBLE
    }

    inner class  SMSTimer( millisInFuture :Long,  countDownInterval:Long):CountDownTimer( millisInFuture  ,  countDownInterval){
        override fun onFinish() {
            getVerifyCode.text="获取验证码"
            getVerifyCode.isClickable=true
        }

        override fun onTick(millisUntilFinished: Long) {
            getVerifyCode.text="已发送"+millisUntilFinished/1000+"s"
        }
    }

    override fun onBackPressed() {
        finish()
    }
    companion object {
        private const val TAG = "LoginActivity"

        @JvmStatic val START_WITH_TRANSITION = "start_with_transition"

        @JvmStatic val INTENT_HAS_NEW_VERSION = "intent_has_new_version"

        @JvmStatic val INTENT_VERSION = "intent_version"

        private val ACTION_LOGIN = "${GifFun.getPackageName()}.ACTION_LOGIN"

        private val ACTION_LOGIN_WITH_TRANSITION = "${GifFun.getPackageName()}.ACTION_LOGIN_WITH_TRANSITION"


        fun actionStart(activity: Activity,hasNewVersion: Boolean, version: Version?) {
            val intent = Intent(ACTION_LOGIN).apply {
                putExtra(INTENT_HAS_NEW_VERSION, hasNewVersion)
                putExtra(INTENT_VERSION, version)
            }
            activity.startActivity(intent)
        }

        fun actionStartWithTransition(activity: Activity, logo: View, hasNewVersion: Boolean, version: Version?) {
            val intent = Intent(ACTION_LOGIN_WITH_TRANSITION).apply {
                putExtra(INTENT_HAS_NEW_VERSION, hasNewVersion)
                putExtra(INTENT_VERSION, version)
            }
            if(AndroidVersion.hasLollipop()){
                intent.putExtra(START_WITH_TRANSITION, true)
                val options=ActivityOptions.makeSceneTransitionAnimation(activity,logo,activity.getString(R.string.transition_logo_splash))
                activity.startActivity(intent,options.toBundle())
            }else{
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }
}
