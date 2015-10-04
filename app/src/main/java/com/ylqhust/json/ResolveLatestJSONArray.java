package com.ylqhust.json;

import android.util.Log;

import com.ylqhust.contract.LatestScheam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/9/28.
 */
public class ResolveLatestJSONArray {
    public static List<LatestScheam> resolve(JSONArray jsonArray) throws JSONException
    {
        if (jsonArray == null)
            return null;
        List<LatestScheam> latestScheams = new ArrayList<LatestScheam>();
        int ObjectCount = jsonArray.length();
        for(int i=0;i<ObjectCount;i++)
        {
            LatestScheam latestScheam = new LatestScheam();
            JSONObject object = jsonArray.getJSONObject(i);
            //main node
            latestScheam.setId(object.getInt("id"));
            latestScheam.setTitle(object.getString("title"));
            latestScheam.setUrl(object.getString("url"));
            latestScheam.setContent(object.getString("content"));
            latestScheam.setContent_rendered(object.getString("content_rendered"));
            latestScheam.setReplies(object.getInt("replies"));
            latestScheam.setCreateTime(object.getLong("created"));
            latestScheam.setLast_modified(object.getLong("last_modified"));
            latestScheam.setLast_touched(object.getLong("last_touched"));

            //member node
            object = object.getJSONObject("member");

            latestScheam.setUser_id(object.getInt("id"));
            latestScheam.setUser_name(object.getString("username"));
            latestScheam.setUser_head_image_mini_url(object.getString("avatar_mini"));
            latestScheam.setUser_head_image_normal_url(object.getString("avatar_normal"));
            latestScheam.setUser_head_image_large_url(object.getString("avatar_large"));

            //node node
            object = jsonArray.getJSONObject(i).getJSONObject("node");

            latestScheam.setNode_id(object.getInt(("id")));
            latestScheam.setNode_name(object.getString("name"));
            latestScheam.setNode_title(object.getString("title"));
            latestScheam.setNode_url(object.getString("url"));
            latestScheam.setNode_topics_count(object.getInt("topics"));
            latestScheam.setNode_image_mini_url(object.getString("avatar_mini"));
            latestScheam.setNode_image_normal_url(object.getString("avatar_normal"));
            latestScheam.setNode_image_large_url(object.getString("avatar_large"));

            latestScheams.add(latestScheam);
        }
        return latestScheams;
    }
}
