package com.ylqhust.contract;

/**
 * Created by apple on 15/10/1.
 */
public class ReplyInfo {
    //回复者的图片
    private String image_url;
    //回复者的用户名
    private String reply_name;
    //回复时间
    private String reply_time;
    //回复楼层
    private String reply_number;
    //回复内容
    private String reply_content;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_name() {
        return reply_name;
    }

    public void setReply_name(String reply_name) {
        this.reply_name = reply_name;
    }

    public String getReply_number() {
        return reply_number;
    }

    public void setReply_number(String reply_number) {
        this.reply_number = reply_number;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    @Override
    public String toString()
    {
        return image_url+"\n"+reply_name+"\n"+reply_number+"\n"+reply_time+"\n"+reply_content;
    }
}
