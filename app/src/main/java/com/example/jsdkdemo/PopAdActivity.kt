package com.example.jsdkdemo

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.widget.Toast
import cn.imeiadx.jsdk.jy.mob.JyAd
import cn.imeiadx.jsdk.jy.mob.JyAdListener2
import cn.imeiadx.jsdk.jy.mob.JyAdPopWindow
import cn.imeiadx.jsdk.jy.mob.JyNative
import kotlinx.android.synthetic.main.activity_pop_ad.*

/**
 * @author liuhuijie
 * @date 2020/10/20
 */
class PopAdActivity : AppCompatActivity() {
    private var mPid = ""
    private var mPopupWindowAd: JyAdPopWindow? = null
    private var listener2: JyAdListener2? = null
    private var offset: Int = 0

    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, PopAdActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_ad)
        initData()
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title="弹出广告"
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
        mPid = PreferencesUtils[PreferencesUtils.POP_PID, ""].toString()
        etPid.setText(if (!TextUtils.isEmpty(mPid)) mPid else Constants.DEFP_ID)
        val localWidth = Integer.valueOf(PreferencesUtils[PreferencesUtils.POP_WIDTH, -1].toString())
        if (localWidth >= -2) {
            etWidth.setText(localWidth.toString())
        }
        val localHeight = Integer.valueOf(PreferencesUtils[PreferencesUtils.POP_HEIGHT, -1].toString())
        if (localHeight >= -2) {
            etHeight.setText(localHeight.toString())
        }

        tvResult.movementMethod = ScrollingMovementMethod()
        tvLoad.setOnClickListener {
            initPopAd()
        }
        tvClose.setOnClickListener {
            if (mPopupWindowAd != null) {
                // val viewGroupParent = findViewById<ViewGroup>(android.R.id.content)
                mPopupWindowAd!!.popupwindow.dismiss()
                mPopupWindowAd=null
            }
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
            }

            override fun onClosed() {
                addResult("onClosed")
                WLog.d("----onClosed")
                runOnUiThread {
                    mPopupWindowAd!!.dismiss()
                    mPopupWindowAd= null
                }
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
    private fun initPopAd() {
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

        if (mPopupWindowAd == null) {
            // new ColorDrawable(0x7DC0C0C0) 半透明灰色
/*            mPid= "H8VHXVPN8AJ0RTPW1K9Q";
            mAdWidth = 640;
            mAdHeight = 960;*/
            mPopupWindowAd = JyAd.initPopWindow(this, mPid, widthValue, heightValue, listener2,
                    ColorDrawable(0x7DC0C0C0), 640, 960, true)
            mPopupWindowAd!!.setOpen(false)
        }
        if (!TextUtils.isEmpty(mPid)) {
            PreferencesUtils.put(PreferencesUtils.POP_PID, mPid)
            PreferencesUtils.put(PreferencesUtils.POP_WIDTH, widthValue)
            PreferencesUtils.put(PreferencesUtils.POP_HEIGHT, heightValue)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mPopupWindowAd!=null){
           mPopupWindowAd!!.dismiss()
        }
        JyAd.exit()
    }

}