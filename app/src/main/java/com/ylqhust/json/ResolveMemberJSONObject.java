package com.ylqhust.json;

import com.ylqhust.contract.MemberScheam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 15/9/30.
 */
public class ResolveMemberJSONObject
{
    public static MemberScheam resolve(JSONObject jsonObject)
    {
        try {
            if (jsonObject != null && "found".equals(jsonObject.getString(MemberScheam.STATUS)))
            {
                MemberScheam memberScheam = new MemberScheam();
                memberScheam.setAvatar_large(jsonObject.getString(MemberScheam.AVATAR_LARGE));
                memberScheam.setAvatar_mini(jsonObject.getString(MemberScheam.AVATAR_MINI));
                memberScheam.setAvatar_normal(jsonObject.getString(MemberScheam.AVATAR_NORMAL));
                memberScheam.setBio(jsonObject.getString(MemberScheam.BIO));
                memberScheam.setBtc(jsonObject.getString(MemberScheam.BTC));
                memberScheam.setCreatedTime(jsonObject.getLong(MemberScheam.CREATED));
                memberScheam.setGithub(jsonObject.getString(MemberScheam.GITHUB));
                memberScheam.setId(jsonObject.getInt(MemberScheam.ID));
                memberScheam.setLocation(jsonObject.getString(MemberScheam.LOCATION));
                memberScheam.setPsn(jsonObject.getString(MemberScheam.PSN));
                memberScheam.setTagLine(jsonObject.getString(MemberScheam.TAGLINE));
                memberScheam.setTwitter(jsonObject.getString(MemberScheam.TWITTER));
                memberScheam.setUrl(jsonObject.getString(MemberScheam.URL));
                memberScheam.setWebsite(jsonObject.getString(MemberScheam.WEBSITE));
                memberScheam.setUsername(jsonObject.getString(MemberScheam.USERNAME));
                return memberScheam;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
