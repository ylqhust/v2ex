package com.ylqhust.resolvehtml;

import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;
import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.contract.ReplyInfo;
import com.ylqhust.v2ex.Global;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by apple on 15/10/15.
 */
public class ResolveOneArticleS extends TextHttpResponseHandler
{
    private UpdateUI<ArticleInfo> updateUI;
    private String url;
    private String htmlString;

    public void setUpdateUI(UpdateUI<ArticleInfo> updateUI)
    {
        this.updateUI = updateUI;
    }

    public ResolveOneArticleS(String url)
    {
        this.url = url;
        Log.i("ResolveOneArticleS", url);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
       onSuccess(statusCode, headers, "null");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        this.htmlString = responseString;
        if (htmlString != null && !htmlString.equals("null"))
            SaveCache();
        else
            this.htmlString = GetCache();

        if (this.htmlString == null)
        {
            this.updateUI.updateUI(null);
            return;
        }
        else
        {
            this.updateUI.updateUI(getARI());
        }
    }

    private ArticleInfo getARI()
    {
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setReplyInfos(ParseReply(htmlString));
        String[] grays = ParseGray(htmlString);
        articleInfo.setInfo(grays[0]);
        articleInfo.setReply_base_info(grays[1]);
        articleInfo.class_tag = ParseClassTag(htmlString);

        return articleInfo;
    }

    private String GetCache() {
        try {
            return Global.diskCache.getString(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void SaveCache() {
        Global.diskCache.delete(url);
        Global.diskCache.save(url,htmlString.getBytes());
    }

    public void resolve()
    {
        Global.v2EXManager.SolveUrlGet(url,this);
    }




    public List<ReplyInfo> ParseReply(String htmlString)
    {
        String CLASS_TAG = "MYSELFCUSTOM";
        String regx = "id=\"r_[0-9]{7}\"\\sclass=\"(inner|cell)\"";
        htmlString = htmlString.replaceAll(regx,"class=\"MYSELFCUSTOM\"");
        Document doc = Jsoup.parse(htmlString);
        org.jsoup.nodes.Element element = doc.body();
        org.jsoup.select.Elements elemetns = element.getElementsByClass(CLASS_TAG);

        List<ReplyInfo> allReplyInfo = new ArrayList<ReplyInfo>();
        for (org.jsoup.nodes.Element element1 : elemetns)
        {
            Elements tmp = null;

            tmp = element1.getElementsByClass("avatar");
            String replyer_img = "http:"+tmp.first().attr("src").toString();

            tmp = element1.getElementsByClass("no");
            String no = tmp.first().text();

            tmp = element1.getElementsByClass("dark");
            String replyer_name = tmp.first().text();

            tmp = element1.getElementsByClass("fade");
            String reply_time = tmp.first().text();

            tmp = element1.getElementsByClass("reply_content");
            String reply_content_html = tmp.first().toString();
            reply_content_html= reply_content_html.replaceAll("<br />","\n").
                    replaceAll("<br>","").
            replaceAll("<br/>","\n");

            Document docc = Jsoup.parse(reply_content_html);
            String reply_content_text = docc.text();
            ReplyInfo replyInfo = new ReplyInfo();

            replyInfo.setImage_url(replyer_img);
            replyInfo.setReply_content(reply_content_text);
            replyInfo.setReply_name(replyer_name);
            replyInfo.setReply_number(no);
            replyInfo.setReply_time(reply_time);

            allReplyInfo.add(replyInfo);
        }
        return allReplyInfo;
    }

    public String[] ParseGray(String htmlString)
    {
        Document doc = Jsoup.parse(htmlString);
        Elements elements = doc.getElementsByClass("gray");

        String create_base_info = elements.first().text();
        String reply_base_info = null;

        if (elements.size() >= 4)
        {
            reply_base_info = elements.get(2).text();
        }
        return new String[]{create_base_info,reply_base_info};
    }

    public String ParseClassTag(String htmlString)
    {
        String regx = "<a href=\"/\">V2EX.*";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(htmlString);
        if(matcher.find())
        {
            Document doc = Jsoup.parse(matcher.group());
            return doc.text();
        }
        return "V2EX";
    }



}
