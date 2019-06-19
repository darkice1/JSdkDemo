package com.example.jsdkdemo;

import android.util.Log;

public class WLog
{
    private static final String TAG="JYAD";

    public static void d (Exception e)
    {
        d(TAG, e.toString());
    }

    public static void d (String msg)
    {
        Log.d(TAG, msg);
    }

    public static void d (String msg,Object ...objects)
    {
        d(String.format(msg, objects));
    }
}

