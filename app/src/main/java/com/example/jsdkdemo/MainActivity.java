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

import cn.imeiadx.jsdk.jy.mob.JyAd;
import cn.imeiadx.jsdk.jy.mob.JyAdListener;
import cn.imeiadx.jsdk.jy.mob.JyAdPopWindow;
import cn.imeiadx.jsdk.jy.mob.JyAdView;

public class MainActivity extends AppCompatActivity {

    private JyAdPopWindow mPopupWindow = null;
    private Activity act = null;
    // 位置ID
    private String pid = "DAOOVC5SHVFTXACPPLL0";

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

        TestJs js = new TestJs();
        act = this;
        JyAdView adv = JyAd.initNormalAdView(this, "MEJDNjhDMDJCMzJEQUI1", 480,320, js,"<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\" />\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<style type=\"text/css\">\n" +
                "*{margin: 0;padding: 0;font-style: normal;text-decoration: none;}\n" +
                "</style>\n" +
                "<body>\n" +
                "<a href=\"__landingpage_url__\" target=\"_blank\">\n" +
                "<div style=\"overflow: hidden;width: 1400px;\">\n" +
                "<div><img src=\"__adurl__\" style=\"float: left;margin: 0;padding: 0;\"></div>\n" +
                "<div style=\"float: left;margin-top: 20px;width: 920px;\">\n" +
                "<p id=\"\" style=\"font-size:2rem;width: 900px;height: 200px;word-wrap: break-word;word-break: normal; margin-left: 20px;color: #333333;\">\n" +
                "__title__\n" +
                "</p>\n" +
                "<p style=\"font-size:1rem;margin-left:20px;color: #808080;\">\n" +
                "广告\n" +
                "</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</a>\n" +
                "</body>\n" +
                "</html>\n");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        params.width = 1400;
        params.height = 320;

        params.gravity = Gravity.BOTTOM;

        this.addContentView(adv, params);

        findViewById(R.id.tvOpenPop).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                getPackageManager();

                if (mPopupWindow == null) {
                    // new ColorDrawable(0x7DC0C0C0) 半透明灰色
                    mPopupWindow = JyAd.initPopWindow(act, pid, 300, 250, null,
                            new ColorDrawable(0x7DC0C0C0));
                    JyAdListener l = new JyAdListener() {
                        @Override
                        public void onClosed() {
                            // 加入广告插屏关闭时响应
                            WLog.d("JyAdListener.onClosed");
                            mPopupWindow = null;
                        }

                    };

                    mPopupWindow.setListener(l); }
            }
        });

        findViewById(R.id.tvClosePop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }

            }
        });

    }
}
