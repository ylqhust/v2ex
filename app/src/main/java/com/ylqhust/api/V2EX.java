package com.ylqhust.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ylqhust.v2ex.Global;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by apple on 15/10/12.
 */
public class V2EX {

    private final static String LOGIN_URL = "http://www.v2ex.com/signin";
    private final static String MISSION_URL = "http://www.v2ex.com/mission/daily";

    private  AsyncHttpClient mClient;
    private Context context;
    private UpdateUserPage updateUserPage;

    final ProgressDialog customDialog = Global.getCustomDialog1("正在登陆...");
    public static void Replay(String content)
    {
    }

    public V2EX(Context context)
    {
        this.context = context;
    }

    public void setUpdateUserPage(UpdateUserPage updateUserPage)
    {
        this.updateUserPage = updateUserPage;
    }

        public void LoginBefore(final String username, final String password) {
            customDialog.show();
            AsyncHttpClient client = getClient();
            client.get(LOGIN_URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String htmlString = new String(responseBody);
                    String once = getOnce(htmlString);
                    LoginProgress(username, password, once);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }

        private void LoginProgress(String username,String password,String once) {
            AsyncHttpClient client = getClient();
            client.addHeader("Referer", LOGIN_URL);
            RequestParams params = new RequestParams();
            params.put("u",username);
            params.put("p",password);
            params.put("next","/");
            params.put("once", once);
            client.post(LOGIN_URL, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (statusCode == 302) {
                        System.out.println("302Success");
                        getUserInfo();
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    System.out.println("200Success");
                    getUserInfo();
                }
            });
        }

        private String getOnce(String htmlString)
        {
            Pattern pattern = Pattern.compile("<input type=\"hidden\" value=\"([0-9]+)\" name=\"once\" />");
            final Matcher matcher = pattern.matcher(htmlString);
            if (matcher.find())
                return matcher.group(1);
            return null;
        }


    private void getUserInfo()
    {
        customDialog.setMessage("正在获取信息...");
        AsyncHttpClient client = getClient();
        client.get(MISSION_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Global.ResetDialog1();
                customDialog.dismiss();
                try {
                    updateUserPage.UUP(new String(responseBody,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 302)
                {
                    Global.ResetDialog1();
                    customDialog.dismiss();
                    try {
                        updateUserPage.UUP(new String(responseBody,"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }





    private AsyncHttpClient getClient() {
        if (mClient == null) {
            mClient = new AsyncHttpClient();
            mClient.setEnableRedirects(false);
            mClient.setCookieStore(new PersistentCookieStore(context));
            mClient.addHeader("Cache-Control", "max-age=0");
            mClient.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            mClient.addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7");
            mClient.addHeader("Accept-Language", "zh-CN, en-US");
            mClient.addHeader("Host", "www.v2ex.com");
            mClient.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
        }
        return mClient;
    }

    public void setmClient()
    {
        this.mClient = null;
    }
    public interface UpdateUserPage{
        public void UUP(String string);
    }
}
