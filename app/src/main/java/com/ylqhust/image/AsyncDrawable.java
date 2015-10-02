package com.ylqhust.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by apple on 15/9/29.
 */
public class AsyncDrawable extends BitmapDrawable
{
    private WeakReference<DownloadImageTask> downloadImageTaskWeakReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, DownloadImageTask downloadImageTask) {
        super(res, bitmap);
        this.downloadImageTaskWeakReference = new WeakReference<DownloadImageTask>(downloadImageTask);
    }

    public DownloadImageTask getDownloadImageTask()
    {
        if (downloadImageTaskWeakReference != null)
        {
            return downloadImageTaskWeakReference.get();
        }
        return null;
    }
}
