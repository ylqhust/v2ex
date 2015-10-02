package com.ylqhust.resolvehtml;

import android.os.AsyncTask;

import com.ylqhust.v2ex.Global;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apple on 15/10/1.
 */
public class GetHtmlTask extends AsyncTask<String,Integer,String> {

    private String urlString;
    private DoAfter doAfter;
    public void setDoAfter(DoAfter doAfter)
    {
        this.doAfter = doAfter;
    }
    public interface DoAfter{
        public void doAfter(String htmlString);
    }
    @Override
    protected void onPreExecute()
    {
    }
    private String readInputStream(InputStream instream) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        instream.close();
        return new String(outStream.toByteArray());
    }
    @Override
    protected String doInBackground(String... params) {
        urlString = params[0];
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200)
            {
                String htmlString = readInputStream(conn.getInputStream());
                if (htmlString != null)
                {
                    //进行本地缓存
                    Global.diskCache.delete(urlString);
                    Global.diskCache.save(urlString,htmlString.getBytes());
                    return htmlString;
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
            return getLocalCacheHtml();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (result == null)
        {
            //获取本地缓存
            result = getLocalCacheHtml();
            if (result == null)
            {
                //本地也没有缓存
                doAfter.doAfter(null);
                return;
            }

        }
        //回调，将获得的htmlString传递给ResolveOneArticle供其解析
        doAfter.doAfter(result);
    }

    private String getLocalCacheHtml() {
        File file = Global.diskCache.getFile(urlString);
        if (file == null)
            return null;
        try {
            InputStream is = new FileInputStream(file);
            return readInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
