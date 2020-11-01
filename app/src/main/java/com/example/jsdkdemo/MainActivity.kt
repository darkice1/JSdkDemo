package com.example.jsdkdemo

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.imeiadx.jsdk.util.OaidManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {
        tvMacCopy.setOnClickListener(this)
        tvIMeiCopy.setOnClickListener(this)
        tvPackageNameCopy.setOnClickListener(this)
        tvOAidCopy.setOnClickListener(this)
        llNormalAd.setOnClickListener(this)
        llPopAd.setOnClickListener(this)
        llSelfAd.setOnClickListener(this)
        llSplashAd.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        tvPackageName!!.text = "PackageName：" + Utlis.getPackageName(this)
        tvMac!!.text = "MAC：" + Utlis.getMac(this)
        tvOAid!!.text = "OAID：" + OaidManager.getOaid(this@MainActivity)
        tvIMei!!.text = "IMEI：" + Utlis.getImei(this)
    }

    private fun goNormalAd() {
        NormalAdActivity.startActivity(this)
    }

    private fun goPopAd() {
        PopAdActivity.startActivity(this)
    }

    private fun goSelfAd() {
        SelfAdActivity.startActivity(this)
    }

    private fun goSplashAd() {
        SplashAdActivity.startActivity(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvPackageNameCopy -> copyText(tvPackageName)
            R.id.tvIMeiCopy -> copyText(tvIMei)
            R.id.tvOAidCopy -> copyText(tvOAid)
            R.id.tvMacCopy -> copyText(tvMac)
            R.id.llSplashAd -> goSplashAd()
            R.id.llNormalAd -> goNormalAd()
            R.id.llPopAd -> goPopAd()
            R.id.llSelfAd -> goSelfAd()
            else -> {
            }
        }
    }

    private fun copyText(view: View?) {
        if (view is TextView) {
            if (!TextUtils.isEmpty(view.text)) {
                val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val text = view.text.toString()
                val mClipData = ClipData.newPlainText("Label", text.substring(text.indexOf("：") + 1))
                cm.primaryClip = mClipData
                Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "复制内容为空", Toast.LENGTH_LONG).show()
            }
        }
    }
}