package com.ylqhust.contract;

/**
 * Created by apple on 15/9/29.
 */
public class MemberScheam {
    public static final String STATUS = "status";
    public static final String ID = "id";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String WEBSITE = "website";
    public static final String TWITTER = "twitter";
    public static final String PSN = "psn";
    public static final String GITHUB = "github";
    public static final String BTC = "btc";
    public static final String LOCATION = "location";
    public static final String TAGLINE = "tagline";
    public static final String BIO = "bio";
    public static final String AVATAR_MINI = "avatar_mini";
    public static final String AVATAR_NORMAL = "avatar_normal";
    public static final String AVATAR_LARGE = "avatar_large";
    public static final String CREATED = "created";


    private int id;
    private String url;
    private String username;
    private String website;
    private String twitter;
    private String psn;
    private String github;
    private String btc;
    private String location;
    private String bio;
    private String tagLine;
    private String avatar_mini = "http:";
    private String avatar_normal = "http:";
    private String avatar_large = "http:";
    private long createdTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large += avatar_large;
    }

    public String getAvatar_mini() {
        return avatar_mini;
    }

    public void setAvatar_mini(String avatar_mini) {
        this.avatar_mini += avatar_mini;
    }

    public String getAvatar_normal() {
        return avatar_normal;
    }

    public void setAvatar_normal(String avatar_normal) {
        this.avatar_normal += avatar_normal;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPsn() {
        return psn;
    }

    public void setPsn(String psn) {
        this.psn = psn;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
