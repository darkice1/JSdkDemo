package com.huaban.android;

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

import com.example.jsdkdemo.R;

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
        JyAdView adv = JyAd.initNormalAdView(this, "AFDMYSFVZ3TYNR4S0EE5", 600,500, js);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        params.width = 600;
        params.height = 500;

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
