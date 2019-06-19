package com.example.jsdkdemo;

import android.util.Log;
import android.webkit.JavascriptInterface;

import cn.imeiadx.jsdk.jy.mob.JyJS;


public class TestJs extends JyJS
{
	@JavascriptInterface  
	public void adcallback()
	{
		WLog.d("没有广告进行回调。");
	}

}
