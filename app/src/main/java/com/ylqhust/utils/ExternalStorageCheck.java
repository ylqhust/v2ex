package com.ylqhust.utils;

import android.os.Environment;

/**
 * Created by apple on 15/9/29.
 */
public class ExternalStorageCheck {
    public static boolean CheckExternalStorage()
    {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return true;
        return false;
    }
}
