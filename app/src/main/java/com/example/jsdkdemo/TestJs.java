package com.example.jsdkdemo;

import android.util.Log;

import cn.imeiadx.jsdk.jy.mob.JyAdListener2;


public class TestJs extends JyAdListener2
{
	@Override
	public void onADReceive() {
		Log.e("ADTEST","onADReceive");
	}

	@Override
	public void onADClicked() {
		Log.e("ADTEST","onADClicked");
	}


	@Override
	public void onNoAD(String msg) {

		super.onADExposure();
		Log.e("ADTEST","onnodate:"+ msg);
	}

	@Override
	public void onADExposure() {
		Log.e("ADTEST","onADExposure");
	}
}
