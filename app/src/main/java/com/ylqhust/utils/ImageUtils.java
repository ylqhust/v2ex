package com.ylqhust.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;

/**
 * Created by apple on 15/9/29.
 */
public class ImageUtils {
    public static DownloadImageTask getDownloadImageTask(ImageView imageView)
    {
        if (imageView != null)
        {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable)
            {
                final AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                return asyncDrawable.getDownloadImageTask();
            }
        }
        return null;
    }

    public static boolean cancelPotentialWork(String urlString,ImageView imageView)
    {
        final DownloadImageTask downloadImageTask = getDownloadImageTask(imageView);
        if (downloadImageTask != null)
        {
            final String url = downloadImageTask.urlString;
            if (url == null || !url.equals(urlString))
            {
                downloadImageTask.cancel(true);
            }
            else
            {
                return false;
            }
        }
        return true;
    }
}
