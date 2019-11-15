package com.huaban.android;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import cn.imeiadx.jsdk.jy.mob.JyAd;
import cn.imeiadx.jsdk.jy.mob.JyAdListener;
import cn.imeiadx.jsdk.jy.mob.JyAdPopWindow;
import cn.imeiadx.jsdk.jy.mob.JyAdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private JyAdPopWindow mPopupWindowAd = null;
    private JyAdView mNormalAd = null;
    private EditText mEtPid, mEtHeight, mEtWidth;
    private RadioGroup mRgOpenType;
    private TextView mTvOpen, mTvClose;
    private String mPid;//位置id
    private int mAdWidth, mAdHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferencesUtils.init(this);
        initView();
        initData();
    }


    private void initView() {
        mEtPid = findViewById(R.id.etPid);
        mEtHeight = findViewById(R.id.etHeight);
        mEtWidth = findViewById(R.id.etWidth);
        mTvClose = findViewById(R.id.tvClose);
        mTvOpen = findViewById(R.id.tvOpen);
        mRgOpenType = findViewById(R.id.rgOpenType);

        mTvOpen.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
    }

    private void initData() {
        mPid=PreferencesUtils.get(PreferencesUtils.PID,"").toString();
        if (!TextUtils.isEmpty(mPid)){
            mEtPid.setText(mPid);
        }else{
            mEtPid.setText("AFDMYSFVZ3TYNR4S0EE5");
            mEtWidth.setText("600");
            mEtHeight.setText("500");
        }
        int localWidth=Integer.valueOf(PreferencesUtils.get(PreferencesUtils.WIDTH,0).toString());
        if (localWidth>0){
            mEtWidth.setText(String.valueOf(localWidth));
        }
        int localHeight=Integer.valueOf(PreferencesUtils.get(PreferencesUtils.HEIGHT,0).toString());
        if (localHeight>0){
            mEtHeight.setText(String.valueOf(localHeight));
        }
        int localType=Integer.valueOf(PreferencesUtils.get(PreferencesUtils.TYPE,0).toString());
        RadioButton radioButton= (RadioButton) mRgOpenType.getChildAt(localType);
        radioButton.setChecked(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOpen:
                open();
                break;
            case R.id.tvClose:
                close();
                break;
            default:
                break;

        }
    }

    private void open() {
        mPid = mEtPid.getText().toString().trim();
        if (TextUtils.isEmpty(mPid)) {
            toast("请输入位置id");
            return;
        }
        String width = getEdText(mEtWidth);
        if (TextUtils.isEmpty(width)) {
            mAdWidth = 0;
        } else {
            mAdWidth = Integer.parseInt(width);
        }
        String height = getEdText(mEtHeight);
        if (TextUtils.isEmpty(height)) {
            mAdHeight = 0;
        } else {
            mAdHeight = Integer.parseInt(height);
        }
        int type = Integer.parseInt(findViewById(mRgOpenType.getCheckedRadioButtonId()).getTag().toString());
        if (type == 0) {
            initNormalAd();
        } else {
            initPopAd();
        }
        if (!TextUtils.isEmpty(mPid)){
            PreferencesUtils.put(PreferencesUtils.PID,mPid);
            PreferencesUtils.put(PreferencesUtils.WIDTH,mAdWidth);
            PreferencesUtils.put(PreferencesUtils.HEIGHT,mAdHeight);
            PreferencesUtils.put(PreferencesUtils.TYPE,type);
        }


    }

    private void close() {
        int tag = Integer.parseInt(findViewById(mRgOpenType.getCheckedRadioButtonId()).getTag().toString());
        if (tag == 0) {
            closeNormalAd();
        } else {
            closePopAd();
        }
    }

    private void initPopAd() {

        if (mPopupWindowAd == null) {
            // new ColorDrawable(0x7DC0C0C0) 半透明灰色
            mPopupWindowAd = JyAd.initPopWindow(MainActivity.this, mPid, mAdWidth, mAdHeight, null,
                    new ColorDrawable(0x7DC0C0C0));
            JyAdListener l = new JyAdListener() {
                @Override
                public void onClosed() {
                    // 加入广告插屏关闭时响应
                    mPopupWindowAd = null;
                }

            };

            mPopupWindowAd.setListener(l);
        }

    }

    private void initNormalAd() {
        close();
        mNormalAd = JyAd.initNormalAdView(this, mPid, mAdWidth,
                mAdHeight, new TestJs());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        params.width = widthPixels;
        params.height = heightPixels*1/3;

        params.gravity = Gravity.BOTTOM;

        this.addContentView(mNormalAd, params);


    }

    private void closePopAd() {
        if (mPopupWindowAd != null) {
            mPopupWindowAd.dismiss();
        }
    }

    private void closeNormalAd() {

        if (mNormalAd != null) {
            ViewGroup viewGroupParent = findViewById(android.R.id.content);
            if (viewGroupParent != null && viewGroupParent.getChildCount() > 0) {
                viewGroupParent.removeView(mNormalAd);
            }
        }

    }

    private String getEdText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
