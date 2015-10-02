package com.ylqhust.json;

import android.os.AsyncTask;
import android.util.Log;

import com.ylqhust.v2ex.Global;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Created by apple on 15/9/28.
 */
public class GetJSONDataTask extends AsyncTask<String,Integer,Integer>
{
    private static final Integer EXECUTE_SUCCESS = 0x000000001;
    private UpdateUI updateUI = null;
    private JSONArray jsonArray;

    public interface UpdateUI
    {
        public void updateUI(JSONArray jsonArray);
    }
    public void setUpdateUI(UpdateUI updateUI)
    {
        this.updateUI = updateUI;
    }


    public void getJsonData(String urlString) throws IOException, JSONException
    {
        //首先通过网络获取数据
        JSONArray jsonArray = null;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        int returnCode = conn.getResponseCode();
        if (returnCode == 200)
        {
            InputStream is = conn.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line=br.readLine())!=null)
            {
                sb.append(line);
            }
            jsonArray = new JSONArray(sb.toString());
            //将新的数据缓存到本地
            Global.diskCache.delete(urlString);
            Global.diskCache.save(urlString,sb.toString().getBytes());
        }
        //如果无法获取，则取得本地缓存数据
        else
        {
            getLocalCacheJson(urlString);
        }
        conn.disconnect();
        this.jsonArray = jsonArray;
        return;
    }


    @Override
    protected void onPreExecute()
    {
        Global.progressDialogSpinner.show();
    }
    @Override
    protected Integer doInBackground(String... params) {
        try
        {
            this.getJsonData(params[0]);
        }
        catch (Exception e)
        {
            Log.i("GSD","JSONEXECPTION");
            //如果报错的话，使用本地缓存数据
            getLocalCacheJson(params[0]);
            e.printStackTrace();
            return this.EXECUTE_SUCCESS;
        }
        return this.EXECUTE_SUCCESS;
    }




    @Override
    protected void onPostExecute(Integer integer)
    {
        if (integer == this.EXECUTE_SUCCESS && this.updateUI != null && this.jsonArray != null)
        {
            this.updateUI.updateUI(this.jsonArray);
        }
        else
        {
            this.updateUI.updateUI(null);
            //当前用户没有网络也没有一份缓存数据
            System.out.println("No NetWork AND Cache");
        }
        Global.progressDialogSpinner.dismiss();
    }

    //使用本地缓存数据
    private void getLocalCacheJson(String urlString)  {
        File file = Global.diskCache.getFile(urlString);

        if (file == null)//没有一份缓存
            return;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line=br.readLine())!=null)
            {
                sb.append(line);
            }
            jsonArray = new JSONArray(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
