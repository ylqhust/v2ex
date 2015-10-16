package com.ylqhust.contract;

import java.util.List;
import java.util.Map;

/**
 * Created by apple on 15/10/1.
 * 帖子信息类
 */
public class ArticleInfo {
    //发帖者的图片链接
    private String author_image;
    //帖子分类信息
    private List<String> article_class;

    public String class_tag = "V2EX";
    //帖子标题
    private String title;
    //帖子创建信息
    private String info = "";
    //帖子内容
    private String content;

    //回帖的基本信息
    private String reply_base_info = "暂时没有回复";

    //帖子回复信息
    private List<ReplyInfo> replyInfos = null;


    public List<String> getArticle_class() {
        return article_class;
    }

    public void setArticle_class(List<String> article_class) {
        this.article_class = article_class;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<ReplyInfo> getReplyInfos() {
        return replyInfos;
    }

    public void setReplyInfos(List<ReplyInfo> replyInfos) {
        this.replyInfos = replyInfos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReply_base_info() {
        return reply_base_info;
    }

    public void setReply_base_info(String reply_base_info) {
        if (reply_base_info == null)
            return;
        this.reply_base_info = reply_base_info;
    }

    /**
    public String getArticle_Tag()
    {
        /**
        if (this.article_class == null)
            return "";
        StringBuilder sb = new StringBuilder();
        int i=1;
        for(;i<article_class.size()-1;i++)
        {
            sb.append(article_class.get(i)+" > ");
        }
        sb.append(article_class.get(i));
        return sb.toString();
    }
    **/
    public String getArticle_Tag()
    {
        return class_tag;
    }

}
