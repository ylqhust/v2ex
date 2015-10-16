package com.ylqhust.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ylqhust.v2ex.Global;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;

/**
 * Created by apple on 15/10/12.
 */
public class V2EX {

    private final static String LOGIN_URL = "http://www.v2ex.com/signin";
    private final static String MISSION_URL = "http://www.v2ex.com/mission/daily";

    private  AsyncHttpClient mClient;
    private Context context;
    private UpdateUserPage updateUserPage;

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
            Global.progressDialogSpinner.setMessage("正在登陆...");
            Global.progressDialogSpinner.show();
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
            params.put("next", "/");
            params.put("once", once);
            client.post(LOGIN_URL, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (statusCode == 302) {
                        onSuccess(statusCode, headers, responseString);
                    } else {
                        //do nothing
                        Global.progressDialogSpinner.dismiss();
                        updateUserPage.UUP(null);
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (statusCode == 200) {
                        onFailure(statusCode, headers, responseString, null);
                    } else {
                        getUserInfo();
                    }
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
        Global.progressDialogSpinner.setMessage("正在获取信息...");
        AsyncHttpClient client = getClient();
        client.get(MISSION_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Global.progressDialogSpinner.dismiss();
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
                    Global.progressDialogSpinner.dismiss();
                    try {
                        updateUserPage.UUP(new String(responseBody,"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void LoginSilence(final String username,final String password)
    {
        final AsyncHttpClient client = getClient();
        client.get(LOGIN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String once = getOnce(responseString);
                AsyncHttpClient client1 = getClient();
                client1.addHeader("Referer",LOGIN_URL);
                RequestParams params = new RequestParams();
                params.put("u",username);
                params.put("p",password);
                params.put("next","/");
                params.put("once",once);
                client1.post(LOGIN_URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200)
                            onFailure(statusCode,headers,responseBody,null);
                        else
                            Global.isLogin = true;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (statusCode == 302)
                            onSuccess(statusCode,headers,responseBody);
                        else
                            Global.isLogin = false;
                    }
                });
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

    public boolean Reply(final String url,final String content,final TextHttpResponseHandler handler)
    {
        AsyncHttpClient client = getClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("ONFAILED");
                if (statusCode == 302)
                    onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String reply_once = getOnce(responseString);
                reply(url,content,reply_once,handler);
            }
        });

        return true;
    }

    private void reply(String url,String content,String reply_once, final TextHttpResponseHandler handler)
    {
        AsyncHttpClient client = getClient();
        if (reply_once == null)
            return;
        System.out.println("ONCE:"+reply_once);
        System.out.println("url"+url);
        client.addHeader("Referer", url);
        RequestParams params = new RequestParams();
        params.put("content",content);
        params.put("once", reply_once);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    onFailure(statusCode, headers, responseBody, null);
                    return;
                } else {
                    handler.onSuccess(statusCode, headers, responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 302)
                    onSuccess(statusCode, headers, responseBody);
                else {
                    handler.onFailure(statusCode, headers, responseBody, null);
                }
            }
        });
    }

    public InputStream getHtmlIS(String url) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        get.addHeader("Accept-Encoding", "gzip, deflate");
        get.addHeader("Accept-Language", "en-US,en;q=0.5");
        get.addHeader("Host", "www.v2ex.com");
        get.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0");
        HttpResponse response = client.execute(get);
        return response.getEntity().getContent();
    }

    public void SolveUrlGet(String url,TextHttpResponseHandler handler)
    {
        AsyncHttpClient client = getClient();
        client.get(url,handler);
    }
}
