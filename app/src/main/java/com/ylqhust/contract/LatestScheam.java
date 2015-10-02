package com.ylqhust.contract;

/**
 * Created by apple on 15/9/28.
 */
public class LatestScheam {
    private int id; //the id of the articl
    private String title;
    private String url;
    private String content;
    private String content_rendered;
    private int replies;
    private long createTime;
    private long last_modified;
    private long last_touched;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(long last_modified) {
        this.last_modified = last_modified;
    }

    public long getLast_touched() {
        return last_touched;
    }

    public void setLast_touched(long last_touched) {
        this.last_touched = last_touched;
    }

    //in member node
    private int user_id;
    private String user_name;
    private String user_head_image_mini_url = "http:";
    private String user_head_image_normal_url = "http:";
    private String user_head_image_large_url = "http:";

    //remain node node
    private int node_id;
    private String node_name;
    private String node_title;
    private String node_url;
    private int node_topics_count;
    private String node_image_mini_url = "http:";
    private String node_image_normal_url = "http:";
    private String node_image_large_url = "http:";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public void setContent_rendered(String content_rendered) {
        this.content_rendered = content_rendered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNode_id() {
        return node_id;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public String getNode_image_large_url() {
        return node_image_large_url;
    }

    public void setNode_image_large_url(String node_image_large_url) {
        this.node_image_large_url += node_image_large_url;
    }

    public String getNode_image_mini_url() {
        return node_image_mini_url;
    }

    public void setNode_image_mini_url(String node_image_mini_url) {
        this.node_image_mini_url += node_image_mini_url;
    }

    public String getNode_image_normal_url() {
        return node_image_normal_url;
    }

    public void setNode_image_normal_url(String node_image_normal_url) {
        this.node_image_normal_url += node_image_normal_url;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getNode_title() {
        return node_title;
    }

    public void setNode_title(String node_title) {
        this.node_title = node_title;
    }

    public int getNode_topics_count() {
        return node_topics_count;
    }

    public void setNode_topics_count(int node_topics_count) {
        this.node_topics_count = node_topics_count;
    }

    public String getNode_url() {
        return node_url;
    }

    public void setNode_url(String node_url) {
        this.node_url = node_url;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_head_image_large_url() {
        return user_head_image_large_url;
    }

    public void setUser_head_image_large_url(String user_head_image_large_url) {
        this.user_head_image_large_url += user_head_image_large_url;
    }

    public String getUser_head_image_mini_url() {
        return user_head_image_mini_url;
    }

    public void setUser_head_image_mini_url(String user_head_image_mini_url) {
        this.user_head_image_mini_url += user_head_image_mini_url;
    }

    public String getUser_head_image_normal_url() {
        return user_head_image_normal_url;
    }

    public void setUser_head_image_normal_url(String user_head_image_normal_url) {
        this.user_head_image_normal_url += user_head_image_normal_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString()
    {
        return getId()+"\n"+getTitle()+"\n"+getUrl()+"\n"+
                getContent()+"\n"+getReplies()+"\n"+getCreateTime()+"\n"+
                getLast_modified()+"\n"+getLast_touched()+"\n"+getUser_id()+"\n"+
                getUser_name()+"\n"+getUser_head_image_mini_url()+"\n"+
                getUser_head_image_normal_url()+"\n"+getUser_head_image_large_url()+"\n"+
                getNode_id()+"\n"+getNode_name()+"\n"+getNode_title()+"\n"+
                getNode_url()+"\n"+getNode_image_mini_url()+"\n"+getNode_image_normal_url()+"\n"+
                getNode_image_large_url()+"\n";
    }
}
