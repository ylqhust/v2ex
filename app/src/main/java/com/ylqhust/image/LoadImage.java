package com.ylqhust.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by apple on 15/9/29.
 */
public class LoadImage {
    //根据给定的边长加载图片
    public static Bitmap decodeFile(String filePath,int reqWidth,int reqHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置这个参数就是只加载图片的基本信息，不对图片进行解码
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight)
    {
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;
        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth)
        {
            final int halfHeight = outHeight / 2;
            final int halfWidth = outWidth / 2;

            while ((halfHeight/inSampleSize) > reqHeight
                    && (halfWidth/inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
