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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.imeiadx.jsdk.jy.mob.JyAd;
import cn.imeiadx.jsdk.jy.mob.JyAdListener2;
import cn.imeiadx.jsdk.jy.mob.JyAdView;

public class MainActivity extends AppCompatActivity {

    private JyAdView wv = null;
    private Activity act = null;
    // 位置ID
    private String pid = "II4RNYYKYBB5O6F9SEFW";

    private PopupWindow popwin;

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

            public void onADClicked() {
                WLog.d("onADClicked");
            }

        };

/*        final JyAdView adv = JyAd.initNormalAdView(this, "II4RNYYKYBB5O6F9SEFW", 640,100,null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        params.width = 640;
        params.height = 100;
        params.gravity = Gravity.BOTTOM;
        this.addContentView(adv, params);*/

        //自定义插屏
        int w= 640,h=960;

        RelativeLayout tp = new RelativeLayout(act);
        wv = JyAd.initNormalAdView(act, "II4RNYYKYBB5O6F9SEFW", -1,-1, listener2);

        popwin = new PopupWindow(tp);
        RelativeLayout.LayoutParams wvl = new RelativeLayout.LayoutParams(w,h);
        wvl.addRule(RelativeLayout.CENTER_IN_PARENT);
        wv.setLayoutParams(wvl);

        RelativeLayout.LayoutParams tpl = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        tpl.addRule(RelativeLayout.CENTER_IN_PARENT);
        tp.setLayoutParams(tpl);

        tp.addView(wv);
        popwin.setBackgroundDrawable(new ColorDrawable(0));
        popwin.setFocusable(false);
        popwin.setWidth(w);
        popwin.setHeight(h);
        popwin.setTouchable(true);
        popwin.setOutsideTouchable(false); //false时点击外面不会被关闭
//        popwin.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,0,0)));

        findViewById(R.id.tvOpenPop).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                getPackageManager();
                popwin.showAtLocation(act.getWindow().getDecorView(),Gravity.CENTER, 0, 0);
                popwin.update();
                wv.refreshDrawableState();
            }
        });

        findViewById(R.id.tvShowPop).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                popwin.showAtLocation(act.getWindow().getDecorView(),Gravity.CENTER, 0, 0);
                popwin.update();
            }
        });

        findViewById(R.id.tvClosePop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                popwin.dismiss();
//                if (wv != null)
                {
//                    adview.dismiss();
//                    wv =null;
                }

            }
        });

    }
}
