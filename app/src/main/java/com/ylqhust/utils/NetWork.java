package com.ylqhust.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by apple on 15/9/30.
 */
public class NetWork {
    public static void GoToWeb(String url,Activity activity)
    {
        Uri webpage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW,webpage);
        activity.startActivity(webIntent);
    }
}
