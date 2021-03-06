package com.ylqhust.v2ex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

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

    public static V2EX v2EXManager;

    public static void AutoLogin()
    {
        SharedPreferences sp = activity.getSharedPreferences("private",Context.MODE_PRIVATE);
        String username = sp.getString("username",null);
        String password = sp.getString("password",null);

        if (username == null || password == null)
            return;
        v2EXManager.LoginSilence(username,password);
    }

    public static void SaveAccountInfo(String username,String password)
    {
        SharedPreferences sp = activity.getSharedPreferences("private", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }

    public static void ClearAccountInfo()
    {
        SharedPreferences sp = activity.getSharedPreferences("private",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }
}
