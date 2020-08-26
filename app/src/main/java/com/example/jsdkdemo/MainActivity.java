package com.example.jsdkdemo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import cn.imeiadx.jsdk.jy.mob.JyAd;
import cn.imeiadx.jsdk.jy.mob.JyAdListener2;
import cn.imeiadx.jsdk.jy.mob.JyAdPopWindow;
import cn.imeiadx.jsdk.jy.mob.JyAdView;
import cn.imeiadx.jsdk.jy.mob.JyNative;

public class MainActivity extends AppCompatActivity {

    private JyAdPopWindow mPopupWindow = null;
    private Activity act = null;
    // 位置ID
//    KCIDGYL0HLUTF3O2U190    安卓视频测试	640x960
//    GL2TTLZJK3JTFWXECFJ1      安卓图片测试	640x960  唤醒
    private String pid = "KCIDGYL0HLUTF3O2U190";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_FINE_LOCATION",
                getPackageName()));

        WLog.d(getPackageName());
        if (permission) {
            WLog.d("有这个权限");
        } else {
            WLog.d("木有这个权限");
        }

        //设置下载路径 默认为Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        JyAd.setDowdloadPath();

        act = this;
        final JyAdListener2 listener2 = new JyAdListener2()
        {
            public void onNoAD(String err)
            {
                WLog.d("JyAdListener.onClosed:"+err);
            }

            public void onADReceive() {
                WLog.d("onADReceive");
            }

            public void onADExposure() {
                WLog.d("onADExposure");

            }
            public  void onADReceive(JyNative jyNative)
            {
                if (jyNative != null)
                {
                    JSONObject detail = jyNative.getDetail();
                    try {
                        //素材地址
                        String adurl = detail.getString("adurl");
//                        WLog.d(adurl);
                        //发送广告展示请求
                        jyNative.show();
                        //执行点击事件并发送请求
                        jyNative.click();

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onADClicked() {
                WLog.d("onADClicked");

            }

            public void onClosed()
            {
                WLog.d("onClosed");
            }
        };

        final JyAdView adv = JyAd.initNormalAdView(this, pid, -1,-1, listener2,false);
        adv.setOpen(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        //是否在开新窗口  true 新开窗口（默认值） false在app内打卡
//        adv.setOpen(false);

        params.width = 320;
        params.height = 480;

        params.gravity = Gravity.BOTTOM;

        this.addContentView(adv, params);

        findViewById(R.id.tvOpenPop).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                getPackageManager();
                if (mPopupWindow == null) {
                    // new ColorDrawable(0x7DC0C0C0) 半透明灰色
                    mPopupWindow = JyAd.initPopWindow(act, pid, -1, -1 , listener2,  new ColorDrawable(0x7DC0C0C0),320,480,true);
                }
            }
        });

        findViewById(R.id.tvClosePop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
/*                JyNative jyNative = JyAd.getNative(act,pid,-1,-1,listener2);
                jyNative.setOpen(true);*/

                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }

            }
        });

    }
}
