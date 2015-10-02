package com.ylqhust.image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.ylqhust.utils.ExternalStorageCheck;
import com.ylqhust.utils.ImageUtils;
import com.ylqhust.utils.MD5;
import com.ylqhust.v2ex.Global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by apple on 15/9/29.
 */
public class DownloadImageTask extends AsyncTask<String,Integer,Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    public String urlString = null;
    private File ImagePath = null;
    private int reqWidth = 50;
    private int reqHeight = 50;
    public DownloadImageTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params)
    {
        this.urlString = params[0];
        this.reqWidth = Integer.valueOf(params[1]);
        this.reqHeight = Integer.valueOf(params[2]);
        //首先加载本地图片缓存
        File oldfile = Global.diskCache.getFile(urlString);
        if (oldfile != null)
        {
            this.ImagePath = oldfile;
        }
        else
        {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                if (conn.getResponseCode() == 200)
                {
                    InputStream is = conn.getInputStream();
                    if (ExternalStorageCheck.CheckExternalStorage())
                    {
                        File file = new File(Environment.getExternalStorageDirectory(),"Cache");
                        file.mkdir();
                        ImagePath = new File(file,MD5.GetMD5Code(params[0]));
                        FileOutputStream fos = new FileOutputStream(ImagePath);
                        byte[] bytes = new byte[1024];
                        int len;
                        while((len = is.read(bytes))!=-1)
                        {
                            fos.write(bytes,0,len);
                            fos.flush();
                        }
                        fos.close();
                        is.close();
                        //图片已经缓存成功，添加信息到map
                        Global.diskCache.put(params[0],ImagePath);
                    }
                    else
                    {
                    }
                }
                conn.disconnect();

            }
            catch(Exception e)
            {
                //报错，加载本地图片缓存

            }
        }

        if (this.ImagePath != null)
        {
            System.out.println("LoadImage");
            return LoadImage.decodeFile(ImagePath.toString(),this.reqWidth,this.reqHeight);
        }
        System.out.println("return null");
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (isCancelled())
        {
            bitmap = null;
        }
        if (this.imageViewWeakReference !=null && bitmap !=null)
        {
            final ImageView imageView = imageViewWeakReference.get();
            final DownloadImageTask downloadImageTask = ImageUtils.getDownloadImageTask(imageView);
            if (this == downloadImageTask && imageView != null)
            {
                imageView.setImageBitmap(bitmap);
            }
        }
    }


}
