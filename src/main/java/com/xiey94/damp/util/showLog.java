package com.xiey94.damp.util;

import android.util.Log;

/**
 * @author : xiey
 * @project name : As30.
 * @package name  : com.xiey94.damp.util.
 * @date : 2018/1/18.
 * @signature : do my best.
 * @explain :
 */

public class showLog {
    public static final String TAG = "ccer";

    public static void show(String msg) {
        Log.e(TAG, "" + msg);
    }

    public static void show(Integer msg) {
        Log.e(TAG, "" + msg);
    }

    public static void show(Boolean msg) {
        Log.e(TAG, "" + msg);
    }

    public static void show(Long msg) {
        Log.e(TAG, "" + msg);
    }

    public static void show(Double msg) {
        Log.e(TAG, "" + msg);
    }
}
