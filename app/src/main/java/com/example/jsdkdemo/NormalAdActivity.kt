package com.example.jsdkdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import cn.imeiadx.jsdk.jy.mob.JyAd
import cn.imeiadx.jsdk.jy.mob.JyAdListener2
import cn.imeiadx.jsdk.jy.mob.JyAdView
import cn.imeiadx.jsdk.jy.mob.JyNative
import kotlinx.android.synthetic.main.activity_normal_ad.*

/**
 * @author liuhuijie
 * @date 2020/10/20
 */
class NormalAdActivity : AppCompatActivity() {
    private var mPid = ""
    private var mNormalAd: JyAdView? = null
    private var listener2: JyAdListener2? = null
    private var offset: Int = 0
    private var hasAd = false

    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, NormalAdActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_ad)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title="普通广告"
        }

        initData()
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

    private fun close(){
        if (mNormalAd != null) {
            if (flContent != null && flContent.childCount > 0) {
                flContent.removeView(mNormalAd)
            }
        }
    }
    private fun initData() {
        mPid = PreferencesUtils[PreferencesUtils.NORMAL_PID, ""].toString()
        etPid.setText(if (!TextUtils.isEmpty(mPid)) mPid else Constants.DEFP_ID)
        val localWidth = Integer.valueOf(PreferencesUtils[PreferencesUtils.NORMAL_WIDTH, -1].toString())
        if (localWidth >= -2) {
            etWidth.setText(localWidth.toString())
        }
        val localHeight = Integer.valueOf(PreferencesUtils[PreferencesUtils.NORMAL_HEIGHT, -1].toString())
        if (localHeight >= -2) {
            etHeight.setText(localHeight.toString())
        }

        tvResult.movementMethod = ScrollingMovementMethod()
        tvLoad.setOnClickListener {
            initNormalAd()
        }
        tvClose.setOnClickListener {
            close()
            mNormalAd=null
            hasAd=false
        }
        tvShow.setOnClickListener {

        }

        listener2 = object : JyAdListener2() {
            override fun onADReceive() {
                WLog.d("----onADReceive")
                addResult("onADReceive")
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
            }

            override fun onADExposure() {
                addResult("onADExposure")
                WLog.d("----onADExposure")
                hasAd = true
                Toast.makeText(this@NormalAdActivity, "加载成功！",Toast.LENGTH_LONG).show()
            }

            override fun onClosed() {
                addResult("onClosed")
                WLog.d("----onClosed")
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


    private fun initNormalAd() {
        if (mNormalAd!=null && hasAd){
            return
        }
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

        mNormalAd = JyAd.initNormalAdView(this, mPid, -1,
                -1, listener2, false)
        mNormalAd!!.isOpen = true
        val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        params.width = -1
        params.height = -1
        flContent.addView(mNormalAd, params)

        if (!TextUtils.isEmpty(mPid)) {
            PreferencesUtils.put(PreferencesUtils.NORMAL_PID, mPid)
            PreferencesUtils.put(PreferencesUtils.NORMAL_WIDTH, widthValue)
            PreferencesUtils.put(PreferencesUtils.NORMAL_HEIGHT, heightValue)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        JyAd.exit()
    }
}