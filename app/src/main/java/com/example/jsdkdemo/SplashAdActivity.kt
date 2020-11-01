package com.example.jsdkdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.widget.Toast
import cn.imeiadx.jsdk.jy.mob.JyAd
import cn.imeiadx.jsdk.jy.mob.JyAdListener2
import cn.imeiadx.jsdk.jy.mob.JyNative
import cn.imeiadx.jsdk.jy.mob.JySplashAd
import kotlinx.android.synthetic.main.activity_self_ad.etHeight
import kotlinx.android.synthetic.main.activity_self_ad.etPid
import kotlinx.android.synthetic.main.activity_self_ad.etWidth
import kotlinx.android.synthetic.main.activity_self_ad.tvClose
import kotlinx.android.synthetic.main.activity_self_ad.tvLoad
import kotlinx.android.synthetic.main.activity_self_ad.tvResult
import kotlinx.android.synthetic.main.activity_self_ad.tvShow

/**
 * @author liuhuijie
 * @date 2020/10/20
 */
class SplashAdActivity : AppCompatActivity() {
    private var mPid = ""
    private var listener2: JyAdListener2? = null
    private var offset: Int = 0
    private var jySplashAd: JySplashAd? = null
    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, SplashAdActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_ad)
        initData()
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title="开屏广告"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initData() {
        mPid = PreferencesUtils[PreferencesUtils.SPLASH_PID, ""].toString()
        etPid.setText(if (!TextUtils.isEmpty(mPid)) mPid else Constants.DEFP_ID)
        val localWidth = Integer.valueOf(PreferencesUtils[PreferencesUtils.SPLASH_WIDTH, -1].toString())
        if (localWidth >= -2) {
            etWidth.setText(localWidth.toString())
        }
        val localHeight = Integer.valueOf(PreferencesUtils[PreferencesUtils.SPLASH_HEIGHT, -1].toString())
        if (localHeight >= -2) {
            etHeight.setText(localHeight.toString())
        }

        tvResult.movementMethod = ScrollingMovementMethod()
        tvLoad.setOnClickListener {
            initSplashAd()
        }
        tvClose.setOnClickListener {

        }
        tvShow.setOnClickListener {
                if (jySplashAd!!.hasAd()) {
                    jySplashAd!!.showAd()
            }
        }

        listener2 = object : JyAdListener2() {
            override fun onADReceive() {
                WLog.d("----onADReceive")
                addResult("onADReceive")
                Toast.makeText(this@SplashAdActivity, "加载成功！",Toast.LENGTH_LONG).show()
            }

            override fun onADReceive(jyNative: JyNative) {
                WLog.d("----JyNative onADReceive")
                WLog.d("素材地址:%s", jyNative.adUrl)
                jyNative.click()
            }

            override fun onADClicked() {
                addResult("onADClicked")
                WLog.d("----onADClicked")
            }

            override fun onNoAD(msg: String) {
                super.onADExposure()
                addResult("onNoAD")
                WLog.d("----onnodate:$msg")
                startActivity(Intent(this@SplashAdActivity, MainActivity::class.java))
                finish()
            }

            override fun onADExposure() {
                addResult("onADExposure")
                WLog.d("----onADExposure")

            }

            override fun onClosed() {
                addResult("onClosed")
                WLog.d("----onClosed")
                startActivity(Intent(this@SplashAdActivity, MainActivity::class.java))
                finish()
            }
        }
    }
    private fun addResult(text: String) {
        runOnUiThread {
            tvResult.append(text + "\n")
            offset = tvResult.lineCount * tvResult.lineHeight
            if (offset > tvResult.height) tvResult.scrollTo(0, offset - tvResult.height)
        }
    }
    private fun initSplashAd() {
        mPid = etPid.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(mPid)) {
            Toast.makeText(this, "请输入位置id",Toast.LENGTH_LONG).show()
            return
        }
        val width: String = etWidth.text.toString().trim()
        val widthValue = if (TextUtils.isEmpty(width)) {
            0
        } else {
            width.toInt()
        }
        val height: String = etHeight.text.toString().trim()
        val heightValue = if (TextUtils.isEmpty(width)) {
            0
        } else {
            height.toInt()
        }
        jySplashAd = JyAd.getSplashAd(this, mPid, -1, -1, 15, listener2)
        jySplashAd!!.loadAd()

        if (!TextUtils.isEmpty(mPid)) {
            PreferencesUtils.put(PreferencesUtils.SPLASH_PID, mPid)
            PreferencesUtils.put(PreferencesUtils.SPLASH_WIDTH, widthValue)
            PreferencesUtils.put(PreferencesUtils.SPLASH_HEIGHT, heightValue)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        JyAd.exit()
    }
}