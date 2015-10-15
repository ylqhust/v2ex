package com.ylqhust.v2ex;

import android.app.Activity;
import android.app.ProgressDialog;

import com.ylqhust.api.V2EX;
import com.ylqhust.cache.DiskCache;
import com.ylqhust.utils.GetAdvices;

/**
 * Created by apple on 15/9/29.
 */
public class Global {
    public static DiskCache diskCache = null;
    public static boolean IfChosed = false;
    public static GetAdvices getAdvices = null;
    public static ProgressDialog progressDialogSpinner = null;
    public static ProgressDialog progressDialogSpinner2 = null;
    public static Activity activity = null;
    public static final String SHOW_URL = "https://www.v2ex.com/api/members/show.json?username=";
    public static final String MEMBER_URL = "http://www.v2ex.com/member/";

    public static boolean isLogin = false;

    public static ProgressDialog getCustomDialog1(String message)
    {
        progressDialogSpinner.setMessage(message);
        return progressDialogSpinner;
    }

    public static void ResetDialog1()
    {
        progressDialogSpinner.setMessage("正在获取数据...");
        return;
    }

    public static V2EX v2EXManager;

}
