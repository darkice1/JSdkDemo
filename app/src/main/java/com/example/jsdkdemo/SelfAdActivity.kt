package com.example.jsdkdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import cn.imeiadx.jsdk.jy.mob.JyAd
import cn.imeiadx.jsdk.jy.mob.JyAdListener2
import cn.imeiadx.jsdk.jy.mob.JyNative
import cn.imeiadx.jsdk.widget.FullVideoView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_self_ad.etHeight
import kotlinx.android.synthetic.main.activity_self_ad.etPid
import kotlinx.android.synthetic.main.activity_self_ad.etWidth
import kotlinx.android.synthetic.main.activity_self_ad.flContent
import kotlinx.android.synthetic.main.activity_self_ad.tvClose
import kotlinx.android.synthetic.main.activity_self_ad.tvLoad
import kotlinx.android.synthetic.main.activity_self_ad.tvResult
import kotlinx.android.synthetic.main.activity_self_ad.tvShow

/**
 * @author liuhuijie
 * @date 2020/10/20
 */
class SelfAdActivity : AppCompatActivity() {
    private var mPid = ""
    private var listener2: JyAdListener2? = null
    private var offset: Int = 0
    private var path: String? = null

    companion object {
        @JvmStatic
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, SelfAdActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_ad)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title="自渲染广告"
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

    private fun initData() {
        mPid = PreferencesUtils[PreferencesUtils.SELF_PID, ""].toString()
        etPid.setText(if (!TextUtils.isEmpty(mPid)) mPid else Constants.DEFP_ID)
        val localWidth = Integer.valueOf(PreferencesUtils[PreferencesUtils.SELF_WIDTH, -1].toString())
        if (localWidth >= -2) {
            etWidth.setText(localWidth.toString())
        }
        val localHeight = Integer.valueOf(PreferencesUtils[PreferencesUtils.SELF_HEIGHT, -1].toString())
        if (localHeight >= -2) {
            etHeight.setText(localHeight.toString())
        }

        tvResult.movementMethod = ScrollingMovementMethod()
        tvLoad.setOnClickListener {
            initSelfAd()
        }
        tvClose.setOnClickListener {

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
                addResult("onADReceive")
                jyNative.click()
                //Log.e("jsdk",jyNative.getAdUrl().toString());
                // JyNative jyNative=new JyNative(this,);
                path = jyNative.adUrl
                if (path != null) {
                    loadAd(path!!, jyNative.isVideo)
                }
                Log.e("jsdk", "onADReceive$path")
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
                Toast.makeText(this@SelfAdActivity, "加载成功！",Toast.LENGTH_LONG).show()
            }

            override fun onClosed() {
                addResult("onClosed")
                WLog.d("----onClosed")
            }
        }
    }

    private fun loadAd(path: String, isVideo: Boolean) {
        if (isVideo) {
            val videoView = FullVideoView(this)
            videoView.layoutParams = FrameLayout.LayoutParams(-1, -1)
            videoView.setVideoPath(path)
            videoView.start()
            flContent.addView(videoView)
        } else {
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.layoutParams = FrameLayout.LayoutParams(-1, -1)
            flContent.addView(imageView)
            Glide.with(this).load(path).into(imageView)
        }
    }


    private fun addResult(text: String) {
        runOnUiThread {
            tvResult.append(text + "\n")
            offset = tvResult.lineCount * tvResult.lineHeight
            if (offset > tvResult.height) tvResult.scrollTo(0, offset - tvResult.height)
        }
    }

    private fun initSelfAd() {
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
        JyAd.getNative(this, flContent, mPid, widthValue, heightValue, listener2)
        if (!TextUtils.isEmpty(mPid)) {
            PreferencesUtils.put(PreferencesUtils.SELF_PID, mPid)
            PreferencesUtils.put(PreferencesUtils.SELF_WIDTH, widthValue)
            PreferencesUtils.put(PreferencesUtils.SELF_HEIGHT, heightValue)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        JyAd.exit()
    }

}